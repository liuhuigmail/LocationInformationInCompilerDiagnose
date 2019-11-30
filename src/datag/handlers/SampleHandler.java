package datag.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.batch.BatchCompiler;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SampleHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		ISelectionService selectionService=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();
		
		ISelection selection=selectionService.getSelection();
		if(selection instanceof IStructuredSelection) {
			
			for(int i=0;i<((IStructuredSelection)selection).size();i++) {
				System.out.println("-----***********-----------");
				IProject project = null;
				Object element = ((IStructuredSelection)selection).toList().get(i);
				if(element instanceof IResource) {    
					project= ((IResource)element).getProject();    
				}else if (element instanceof IJavaElement) {    
					IJavaProject jProject= ((IJavaElement)element).getJavaProject();    
					project = jProject.getProject();    
				}
				if(project==null) continue;
				
				
				final IJavaProject selectedProject = JavaCore.create(project);
				System.out.println(selectedProject.getElementName());
				Generation g=new Generation(project,selectedProject);
				g.gener();
				System.out.println("ahh");
				System.out.println(i);
				System.out.println(((IStructuredSelection)selection).size());
			}
		}
		return null;
	}
}
