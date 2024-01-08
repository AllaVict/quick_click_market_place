package quick.click.commons.exeptions;

public class ResourceNotFoundException extends QCApplicationException {

	private final Class<?> resourceClass;

	public ResourceNotFoundException(final String message) {
		super(message);
		this.resourceClass = null;
	}

	public ResourceNotFoundException(final String message, final Class<?> resourceClass) {
		super(message);
		this.resourceClass = resourceClass;
	}

	public Class<?> getResourceClass() {
		return resourceClass;
	}
}
