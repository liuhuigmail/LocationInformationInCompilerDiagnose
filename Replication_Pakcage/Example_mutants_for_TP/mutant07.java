package com.application.areca.impl.copypolicy;
import java.io.File;
import com.myJava.file.copypolicy.CopyPolicyException;
/** 
 * <BR>
 * @author Olivier PETRUCCI<BR>
 */
publicalwaysOverwriteCopyPolicy extends AbstractCopyPolicy {
  public boolean acceptImpl(  File file) throws CopyPolicyException {
    return true;
  }
}
