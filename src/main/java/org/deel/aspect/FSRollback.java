package org.deel.aspect;

import java.util.HashMap;
import java.util.UUID;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Aspect
public class FSRollback {
	
	
	private interface Action{
		public void undo();
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
	}
	
	private HashMap<String, Action> undoList = new HashMap<String, Action>();
	
	@Before("execution(* *uploadFile(..))")
	public void advice() {
		System.out.println("**** Setting current transaction id *****");
		
		UUID uuid = UUID.randomUUID();
		TransactionSynchronizationManager.setCurrentTransactionName(uuid.toString());
	}

	@AfterReturning("args(path, ..) && execution(* *savePath(..))")
	public void setIdAdvice(String path) {
		System.out.println("***** Putting action in undo list*******");
		String uuid = TransactionSynchronizationManager.getCurrentTransactionName();
		undoList.put(uuid.toString(), new SaveAction(path));
	}
	
	@AfterReturning("execution(* *uploadFile(..))")
	public void committedAdvice() {
		System.out.println("****** Removing fro undo list**********");
		String uuid = TransactionSynchronizationManager.getCurrentTransactionName();
		undoList.remove(uuid);
	}
	
	@AfterThrowing("execution(* *uploadFile(..))")
	public void rollbackAdvice() {
		System.out.println("************ trying to undo action *******");
		String uuid = TransactionSynchronizationManager.getCurrentTransactionName();
		Action a = undoList.get(uuid);
		if (a != null)
			a.undo();
	}
	
}
