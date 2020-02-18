package com.general.mbts4ma.view.framework.util;

import java.util.ArrayList;

import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.CtScanner;

public class ASTSpoonScanner extends CtScanner {
	
	@Override
	protected void enter(CtElement e) {
		System.out.println(e.toString() + e.getReferencedTypes().toString());
	}
	
	public ArrayList<CtElement> visitStatementAST(CtStatement statement) {
		
		final ArrayList<CtElement> elementsList = new ArrayList<CtElement>();
		
		CtScanner sc = new CtScanner() {

    		public void add(CtElement element) {
    			elementsList.add(element);
    		}

    		@Override
    		public <T> void visitCtLocalVariable(CtLocalVariable<T> localVariable) {
    			super.visitCtLocalVariable(localVariable);
    			add(localVariable);
    		}
    		
    		@Override
    		public <T,A extends T> void visitCtAssignment(CtAssignment<T,A> variableAssignment) {
    			super.visitCtAssignment((CtAssignment<T, A>) variableAssignment);
    			add(variableAssignment);
    		}
    		
    		@Override
    		public <T> void visitCtInvocation(CtInvocation<T> invocation) {
    			super.visitCtInvocation(invocation);    		
    			//invocation.getArguments().clear();
    			add(invocation);
    		}
    		
    		@Override
    		public <T> void visitCtConstructorCall(CtConstructorCall<T> constructorCall) {
    			super.visitCtConstructorCall(constructorCall);
    			add(constructorCall);
    		}
    		
    	};

    	sc.scan(statement);
    	
    	return elementsList;
	}
	
}
