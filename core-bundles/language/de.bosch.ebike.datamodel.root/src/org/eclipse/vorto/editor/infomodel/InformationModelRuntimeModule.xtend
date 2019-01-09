/** 
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package
/*
 * generated by Xtext
 */
org.eclipse.vorto.editor.infomodel

import com.google.inject.Binder
import com.google.inject.Provides
import org.eclipse.vorto.editor.datatype.converter.DatatypeValueConverter
import org.eclipse.vorto.editor.functionblock.validation.TypeFileAccessingHelper
import org.eclipse.vorto.editor.functionblock.validation.TypeHelper
import org.eclipse.vorto.editor.infomodel.formatting.InformationModelFormatter
import org.eclipse.vorto.editor.infomodel.scoping.InformationModelScopeProvider
import org.eclipse.xtext.conversion.IValueConverterService
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.scoping.IScopeProvider

/** 
 * Use this class to register components to be used at runtime / without the
 * Equinox extension registry.
 */
class InformationModelRuntimeModule extends AbstractInformationModelRuntimeModule {
	override void configure(Binder binder) {
		super.configure(binder)
//		binder.bind(IOutputConfigurationProvider).to(InformationModelOutputConfigurationProvider).in(Singleton)
	}

	override Class<? extends IScopeProvider> bindIScopeProvider() {
		return InformationModelScopeProvider
	}
	
	@Provides def TypeHelper getTypeHelper() {
		return new TypeFileAccessingHelper()
	}

	override Class<? extends IQualifiedNameProvider> bindIQualifiedNameProvider() {
		return QualifiedNameWithVersionProvider
	}

	override Class<? extends IValueConverterService> bindIValueConverterService() {
		return DatatypeValueConverter
	}
	
	override bindIFormatter(){
		return InformationModelFormatter
	}
}
