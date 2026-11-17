package dev.robertbian.inventory;

import jakarta.persistence.OptimisticLockException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

public class InventoryErrors {
    @Provider
    public static class InsufficientStock implements ExceptionMapper<IllegalStateException> {
        @Override public Response toResponse(IllegalStateException error) {
            return Response.status(409).entity(Map.of("error", error.getMessage())).build();
        }
    }

    @Provider
    public static class ConcurrentUpdate implements ExceptionMapper<OptimisticLockException> {
        @Override public Response toResponse(OptimisticLockException error) {
            return Response.status(409).entity(Map.of("error", "Concurrent update; retry the request")).build();
        }
    }
}
