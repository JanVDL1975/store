package com.example.store.mapper;

import com.example.store.dto.CustomerDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import com.example.store.dto.OrderCustomerDTO;
import com.example.store.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {CombinedMapperHelper.class})
public interface OrderMapper {
    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "description", target = "orderDescription")
    @Mapping(source = "customer", target = "customer", qualifiedByName ="mapListOfItemsToStringStatic" ) // Map entire customer object
    @Mapping(source = "products", target = "products")
    OrderDTO orderToOrderDTO(Order order);

    // Mapping Customer to CustomerDTO
    @Named("orderCustomerToCustomerDTO")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "customerOrders", target = "customerOrders", qualifiedByName ="listToString")
    CustomerDTO orderCustomerToCustomerDTO(Customer customer);

    @Mapping(source = "products", target = "products", qualifiedByName = "mapStringToProducts")
    Order orderDTOToOrder(OrderDTO orderDTO);

    List<OrderDTO> ordersToOrderDTOs(List<Order> orders);

    @Named("listToString")
    default String listToString(List<String> products) {
        CombinedMapperHelper helper = new CombinedMapperHelper();
        return helper.mapListOfItemsToStringStatic(products);
    }

    @Named("stringToList")
    default List<String> stringToList(String products) {
        CombinedMapperHelper helper = new CombinedMapperHelper();
        return helper.mapStringToListOfItems(products);
    }
    @Named("mapOrdersToIds")
    default String mapOrdersToIds(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return "";
        }

        StringBuilder orderDescriptions = new StringBuilder();
        for (Order order : orders) {
            if (orderDescriptions.length() > 0) {
                orderDescriptions.append(", ");
            }
            orderDescriptions.append(order.getDescription());
        }

        return orderDescriptions.toString();
    }


    @Mapping(source = "customer", target = "customerId", qualifiedByName = "orderCustomerId")
    @Mapping(source = "customer", target = "customerName", qualifiedByName = "orderCustomerName")
    @Mapping(source = "customer", target = "customerOrders", qualifiedByName = "orderCustomerOrders")
    public default OrderCustomerDTO orderToOrderCustomerDTO(Order order) {
        if (order == null) {
            return null;
        }

        OrderCustomerDTO orderCustomerDTO = new OrderCustomerDTO();

        // Assuming these methods extract customer details from the Order
        orderCustomerDTO.setCustomerId(orderCustomerId(order));
        orderCustomerDTO.setCustomerName(orderCustomerName(order));

        // Mapping orders to a concatenated string
        orderCustomerDTO.setCustomerOrders(orderCustomerOrders(order));

        return orderCustomerDTO;
    }

    // Assuming the `Order` object has a `Customer` field or method to get the customer
    @Named("orderCustomerOrders")
    default String orderCustomerOrders(Order order) {
        return order.getCustomer() != null
                ? String.join(", ", order.getCustomer().getCustomerOrders().stream().map(Order::toString).collect(Collectors.toList()))
                : "";
    }

    public default String orderCustomerName(Order order) {
        // Assuming Order has a method `getCustomer` and Customer has a `getName()` method
        return order.getCustomer() != null ? order.getCustomer().getName() : null;
    }

    public default Long orderCustomerId(Order order) {
        // Assuming Order has a method `getCustomer` and Customer has a `getId()` method
        return order.getCustomer() != null ? order.getCustomer().getId() : null;
    }

    // Helper method to convert a List<Product> to a concatenated string
    @Named("mapProductsToString")
    default String mapProductsToString(List<Product> products) {
        if (products != null) {
            return products.stream().map(Product::toString).collect(Collectors.joining(", "));
        }
        return "";
    }
}
