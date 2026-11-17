package dev.robertbian.inventory;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/inventory")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InventoryResource {
    @Inject InventoryService service;

    @POST
    public Uni<InventoryService.ItemView> create(@Valid CreateItem body) {
        return service.create(body.sku(), body.available());
    }

    @GET
    @Path("/{sku}")
    public Uni<InventoryService.ItemView> get(@PathParam("sku") String sku) {
        return service.get(sku);
    }

    @POST
    @Path("/{sku}/reservations")
    public Uni<InventoryService.ItemView> reserve(@PathParam("sku") String sku,
                                                   @Valid ReserveItem body) {
        return service.reserve(sku, body.quantity());
    }

    public record CreateItem(@NotBlank @Size(max = 64) String sku, @Min(0) int available) {}
    public record ReserveItem(@Min(1) int quantity) {}
}
