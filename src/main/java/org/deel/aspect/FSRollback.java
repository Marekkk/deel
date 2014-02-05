package org.deel.aspect;

import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.deel.domain.FileRevision;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Aspect
public class FSRollback {
	
	
	private interface Action{
		public void redo();
	}
	
	private class SaveAction implements Action{
		private String path;
		
		public SaveAction(String path) {
			this.path = new String(path);
		}
		public void redo() {
			System.out.println("**** Removing file " + path + " ******");
			java.io.File f = new java.io.File(path);
			if (f.exists())
				f.delete();
		}
	}
	
	private HashMap<String, Action> redoList = new HashMap<String, Action>();
	
	@Before("execution(* *uploadFile(..))")
	public void advice() {
		System.out.println("**** Setting current transaction id *****");
		
		UUID uuid = UUID.randomUUID();
		TransactionSynchronizationManager.setCurrentTransactionName(uuid.toString());
	}

	@AfterReturning("args(path, ..) && execution(* *savePath(..))")
	public void setIdAdvice(String path) {
		System.out.println("***** Putting action in redo list*******");
		String uuid = TransactionSynchronizationManager.getCurrentTransactionName();
		redoList.put(uuid.toString(), new SaveAction(path));
	}
	
	@AfterReturning("execution(* *uploadFile(..))")
	public void committedAdvice() {
		System.out.println("****** Removing fro redo list**********");
		String uuid = TransactionSynchronizationManager.getCurrentTransactionName();
		redoList.remove(uuid);
	}
	
	@AfterThrowing("execution(* *uploadFile(..))")
	public void rollbackAdvice() {
		System.out.println("************ trying to redo action *******");
		String uuid = TransactionSynchronizationManager.getCurrentTransactionName();
		Action a = redoList.get(uuid);
		if (a != null)
			a.redo();
	}
	
}
