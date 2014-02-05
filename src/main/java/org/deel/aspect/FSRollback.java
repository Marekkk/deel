package org.deel.aspect;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.deel.service.utils.FSUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Aspect
public class FSRollback {


	private interface Action{
		public void undo();
		public void andFinally();
	}

	private class SaveAction implements Action{
		private String path;

		public SaveAction(String path) {
			this.path = new String(path);
		}
		public void undo() {
			System.out.println("**** Removing file " + path + " ******");
			java.io.File f = new java.io.File(path);
			if (f.exists())
				f.delete();
		}
		@Override
		public void andFinally() {}
	}

	private class DeleteAction implements Action {
		private String path;
		private String tmpPath;

		public DeleteAction(String p) {
			path =  p;

			try {
				System.out.println("******** creating tmp file ********");
				this.tmpPath = path + ".removed";
				java.io.File debug = new java.io.File(path);
				if(!debug.exists())
					System.out.println("****** file do not exists ****");
				FSUtils.mv(path, tmpPath);
			} catch (Exception e) {
				/* likely also deleteFile will throw an exception */
				e.printStackTrace();
				throw new RuntimeException("Failed to recover file " + path + " from a rollback delete transaction");
			}
		}


		@Override
		public void undo() {
			System.out.println("************* restoring file ***********");
			if (tmpPath != null) {
				java.io.File f = new java.io.File(this.path);
				java.io.File tmpFile = new java.io.File(this.tmpPath);
				if (f.exists()) {
					if (tmpFile.exists()) {
						tmpFile.delete();
					}
					return;
				}

				try {
					if (tmpFile.exists()) {
						FSUtils.mv(tmpPath, path);
						tmpFile.delete();
					}
				} catch (Exception e) {
					/* likely also deleteFile will throw an exception */
					e.printStackTrace();				}
			}
		}

		@Override
		public void andFinally(){
			java.io.File tmpFile = new java.io.File(this.tmpPath);
			if (tmpFile.exists()) 
				tmpFile.delete();
		}
	}

	private HashMap<String, List<Action>> undoList = new HashMap<String, List<Action>>();

	@Before("execution(* org.deel.service.FileService.*(..))")
	public void advice() {
		System.out.println("**** Setting current transaction id *****");

		UUID uuid = UUID.randomUUID();
		TransactionSynchronizationManager.setCurrentTransactionName(uuid.toString());
		undoList.put(uuid.toString(), new LinkedList<FSRollback.Action>());
	}

	@AfterReturning("args(path, ..) && (execution(* *savePath(..)) || execution(* *mkdir(..)))")
	public void setIdAdvice(String path) {
		System.out.println("***** Putting action in undo list*******");
		String uuid = TransactionSynchronizationManager.getCurrentTransactionName();
		if (uuid == null)
			return;
		
		List<Action> al = undoList.get(uuid);
		
		if (al == null)
			return;
		
		al.add(new SaveAction(FSUtils.getStoragePath() + path));
	}

	@Before("args(path, ..) && execution(* *deleteFile(..))")
	public void deleteAdvice(String path) {
		System.out.println("***** Putting action in undo list*******");
		String uuid = TransactionSynchronizationManager.getCurrentTransactionName();
		undoList.get(uuid).add(new DeleteAction(FSUtils.getStoragePath() + path));
	}

	
	@AfterReturning("execution(* org.deel.service.FileService.*(..))")
	public void committedAdvice() {
		System.out.println("****** Removing fro undo list**********");
		String uuid = TransactionSynchronizationManager.getCurrentTransactionName();
		List<Action> list = undoList.remove(uuid);
		
		for (Action a : list) {
			if (a != null)
				a.andFinally();
		}
		
	}

	@AfterThrowing("execution(* org.deel.service.FileService.*(..))")
	public void rollbackAdvice() {
		System.out.println("************ trying to undo action *******");
		String uuid = TransactionSynchronizationManager.getCurrentTransactionName();

		List<Action> list = undoList.remove(uuid);
		
		for (Action a : list) {
			if (a != null)
				a.undo();
		}
	}

}
