package biancso.mevius.pipeline;

import java.util.List;
import java.util.function.Supplier;

public interface Pipeline {

	public static final Supplier<Pipeline> DEFAULT_PIPELINE = () -> new DefaultPipelineImpl();

	/**
	 * Adds a handler first to be processed
	 * 
	 * @param handler The handler to add
	 * @return The handler's current position on the order
	 */
	public int addFirst(PipelineHandler handler);
	
	/**
	 * Adds a handler last to be processed
	 * 
	 * @param handler The handler to add
	 * @return The handler's current position on the order
	 */
	public int addLast(PipelineHandler handler);
	
	/**
	 * Gets a {@link PipelineHandler} by its ordered ID
	 * 
	 * @param id The ID of the pipeline handler
	 * @return A {@link PipelineHandler}
	 */
	public PipelineHandler getFromId(int id);
	
	/**
	 * Deletes a {@link PipelineHandler} by its ordered ID
	 * 
	 * @param id The ID of the {@link PipelineHandler} to delete
	 * @return The deleted {@link PipelineHandler}, <code>null</code> if none exists
	 */
	public PipelineHandler delete(int id);
	
	public Object process(Object message);

}

class DefaultPipelineImpl
implements Pipeline {

	private List<PipelineHandler> handlers;
	
	{
		handlers.add(PipelineHandler.defaultHandler);
	}
	
	@Override
	public int addFirst(PipelineHandler handler) {
		handlers.add(1, handler);
		return 1;
	}

	@Override
	public int addLast(PipelineHandler handler) {
		handlers.add(handler);
		return handlers.size();
	}

	@Override
	public PipelineHandler getFromId(int id) {
		return handlers.get(id);
	}

	@Override
	public PipelineHandler delete(int id) {
		return handlers.remove(id);
	}

	@Override
	public Object process(Object message) {
		Object cmsg = message;
		
		for(PipelineHandler ph : handlers)
			cmsg = ph.mutate(cmsg);
		
		return cmsg;
	}

}
