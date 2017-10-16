package application.util;

import org.eclipse.jgit.errors.UnsupportedCredentialItem;
import org.eclipse.jgit.transport.CredentialItem;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.URIish;

public class AllowHostsCredentialsProvider extends CredentialsProvider {

	@Override
	public boolean get(URIish uri, CredentialItem... items) throws UnsupportedCredentialItem {
		for(CredentialItem item : items){
			if(item instanceof CredentialItem.YesNoType){
				((CredentialItem.YesNoType) item).setValue(true);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isInteractive() {
		return false;
	}

	@Override
	public boolean supports(CredentialItem... items) {
		for(CredentialItem item : items){
			if(item instanceof CredentialItem.YesNoType){
				return true;
			}
		}
		return false;
	}
}
