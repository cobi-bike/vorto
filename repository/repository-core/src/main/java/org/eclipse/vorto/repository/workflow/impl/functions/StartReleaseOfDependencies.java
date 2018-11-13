package org.eclipse.vorto.repository.workflow.impl.functions;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.eclipse.vorto.repository.workflow.WorkflowException;
import org.eclipse.vorto.repository.workflow.impl.SimpleWorkflowModel;
import org.eclipse.vorto.repository.workflow.model.IWorkflowFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartReleaseOfDependencies implements IWorkflowFunction {

	private IModelRepository repository;
	private IWorkflowService workflowService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StartReleaseOfDependencies.class);
	
	public StartReleaseOfDependencies(IModelRepository repository, IWorkflowService workflowService) {
		this.repository = repository;
		this.workflowService = workflowService;
	}
	
	@Override
	public void execute(ModelInfo model, IUserContext user) throws WorkflowException {
		LOGGER.debug("Executing workflow function: "+this.getClass());
		
		Set<ModelInfo> references = getReferencesRecursive(model);
				
		Optional<ModelInfo> modelByOtherUser = references.stream()
												.filter(modelInfo -> modelInfo.getState().equals(SimpleWorkflowModel.STATE_DRAFT.getName()))
												.filter(modelInfo -> !modelInfo.getAuthor().equals(user.getUsername())).findAny();
		
		if (modelByOtherUser.isPresent()) {
			throw new WorkflowException(modelByOtherUser.get(),"Cannot release dependent model '"+modelByOtherUser.get().getId()+"' because it is owned by another user.");
		}
		
		for (ModelInfo reference : references) {
			if (reference.getState().equals(SimpleWorkflowModel.STATE_DRAFT.getName())) {
				this.workflowService.doAction(reference.getId(), user, SimpleWorkflowModel.ACTION_RELEASE.getName());
			}
		}
	}
	
	private Set<ModelInfo> getReferencesRecursive(final ModelInfo model) {
		Set<ModelInfo> references = new HashSet<ModelInfo>();
		for (ModelId referenceId : model.getReferences()) {
			ModelInfo reference = this.repository.getById(referenceId);
			references.add(reference);
			references.addAll(getReferencesRecursive(reference));
		}		
		return references;
	}

}
