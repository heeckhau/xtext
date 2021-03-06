/**
 * <copyright>
 * </copyright>
 *
 */
package org.eclipse.xtext.xbase.annotations.xAnnotations.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.xtext.xbase.annotations.xAnnotations.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class XAnnotationsFactoryImpl extends EFactoryImpl implements XAnnotationsFactory
{
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static XAnnotationsFactory init()
	{
		try
		{
			XAnnotationsFactory theXAnnotationsFactory = (XAnnotationsFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/Xtext/Xbase/XAnnotations"); 
			if (theXAnnotationsFactory != null)
			{
				return theXAnnotationsFactory;
			}
		}
		catch (Exception exception)
		{
			EcorePlugin.INSTANCE.log(exception);
		}
		return new XAnnotationsFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public XAnnotationsFactoryImpl()
	{
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass)
	{
		switch (eClass.getClassifierID())
		{
			case XAnnotationsPackage.XANNOTATION: return createXAnnotation();
			case XAnnotationsPackage.XANNOTATION_ELEMENT_VALUE_PAIR: return createXAnnotationElementValuePair();
			case XAnnotationsPackage.XANNOTATION_ELEMENT_VALUE_BINARY_OPERATION: return createXAnnotationElementValueBinaryOperation();
			case XAnnotationsPackage.XANNOTATION_VALUE_ARRAY: return createXAnnotationValueArray();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public XAnnotation createXAnnotation()
	{
		XAnnotationImpl xAnnotation = new XAnnotationImpl();
		return xAnnotation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public XAnnotationElementValuePair createXAnnotationElementValuePair()
	{
		XAnnotationElementValuePairImpl xAnnotationElementValuePair = new XAnnotationElementValuePairImpl();
		return xAnnotationElementValuePair;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public XAnnotationElementValueBinaryOperation createXAnnotationElementValueBinaryOperation()
	{
		XAnnotationElementValueBinaryOperationImpl xAnnotationElementValueBinaryOperation = new XAnnotationElementValueBinaryOperationImpl();
		return xAnnotationElementValueBinaryOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public XAnnotationValueArray createXAnnotationValueArray()
	{
		XAnnotationValueArrayImpl xAnnotationValueArray = new XAnnotationValueArrayImpl();
		return xAnnotationValueArray;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public XAnnotationsPackage getXAnnotationsPackage()
	{
		return (XAnnotationsPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static XAnnotationsPackage getPackage()
	{
		return XAnnotationsPackage.eINSTANCE;
	}

} //XAnnotationsFactoryImpl
