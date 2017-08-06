package biancso.mevius.pipeline;

public interface PipelineHandler {

	/**
	 * Changes an object (message) to another one (output)<br>
	 * the new object keeps on getting mutated until the end of the order
	 * 
	 * @param message The message to change (mutate)
	 * @return The new message
	 */
	public Object mutate(Object message);
	
	public static final PipelineHandler defaultHandler = message -> message;

}
