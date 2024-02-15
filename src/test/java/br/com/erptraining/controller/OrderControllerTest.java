package br.com.erptraining.controller;

import br.com.erptraining.domain.Order;
import br.com.erptraining.domain.OrderItem;
import br.com.erptraining.dtos.order.DetailOrderDTO;
import br.com.erptraining.dtos.order.DiscountOrderDTO;
import br.com.erptraining.dtos.orderitem.CreateOrderItemDTO;
import br.com.erptraining.dtos.orderitem.DetailOrderItem;
import br.com.erptraining.dtos.orderitem.UpdateOrderItemDTO;
import br.com.erptraining.dtos.product.DetailProductDTO;
import br.com.erptraining.enums.DiscountOrigin;
import br.com.erptraining.mapper.ProductMapper;
import br.com.erptraining.service.order.OrderItemService;
import br.com.erptraining.service.order.UpdateOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RequiredArgsConstructor
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private JacksonTester<CreateOrderItemDTO> createOrderData;

    private JacksonTester<DetailOrderDTO> detailOrderData;

    @MockBean
    private OrderItemService orderItemService;

    @MockBean
    private UpdateOrderService update;


    @Test
    @DisplayName("Create: Should return HTTP 200 when the input data are valid")
    void create_second_scenario() throws Exception {

        CreateOrderItemDTO createData = new CreateOrderItemDTO(
                UUID.fromString("8003f4df-5cae-4c7e-a597-392c3046f0dc"),
                2
        );

        Order mockedOrder = Mockito.mock(Order.class);

        when(orderItemService.saveOnNewOrder(ArgumentMatchers.any())).thenReturn(mockedOrder);

        MvcResult result = mvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createData))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(mockedOrder.getId()))
                .andExpect(jsonPath("$.orderNumber").value(mockedOrder.getOrderNumber()))
                .andExpect(jsonPath("$.orderItems").isArray())
                .andExpect(jsonPath("$.orderDiscount").value(mockedOrder.getOrderDiscount()))
                .andExpect(jsonPath("$.totalOrder").value(mockedOrder.getTotalOrder()))
                .andExpect(jsonPath("$.orderStatus").value(mockedOrder.getOrderStatus()))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        DetailOrderDTO detailedOrder = objectMapper.readValue(responseBody, DetailOrderDTO.class);

        assertThat(detailedOrder.id()).isEqualTo(mockedOrder.getId());
        assertThat(detailedOrder.orderNumber()).isEqualTo(mockedOrder.getOrderNumber());
        assertThat(detailedOrder.orderItems()).hasSize(mockedOrder.getOrderItems().size());
        assertThat(detailedOrder.orderDiscount()).isEqualTo(mockedOrder.getOrderDiscount());
        assertThat(detailedOrder.totalOrder()).isEqualTo(mockedOrder.getTotalOrder());
        assertThat(detailedOrder.orderStatus()).isEqualTo(mockedOrder.getOrderStatus());
    }


    @Test
    @DisplayName("Create on Existing: Should return HTTP 200 when the input data are valid")
    void addToExistingOrder_second_scenario() throws Exception {

        UUID orderId = UUID.randomUUID();

        CreateOrderItemDTO createData = new CreateOrderItemDTO(
                UUID.fromString("8003f4df-5cae-4c7e-a597-392c3046f0dc"),
                2
        );

        Order mockedOrder = Mockito.mock(Order.class);

        when(orderItemService.saveOnExistingOrder(any(), any())).thenReturn(mockedOrder);

        MvcResult result = mvc.perform(post("/api/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createData))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(mockedOrder.getId()))
                .andExpect(jsonPath("$.orderNumber").value(mockedOrder.getOrderNumber()))
                .andExpect(jsonPath("$.orderItems").isArray())
                .andExpect(jsonPath("$.orderDiscount").value(mockedOrder.getOrderDiscount()))
                .andExpect(jsonPath("$.totalOrder").value(mockedOrder.getTotalOrder()))
                .andExpect(jsonPath("$.orderStatus").value(mockedOrder.getOrderStatus()))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        DetailOrderDTO detailedOrder = objectMapper.readValue(responseBody, DetailOrderDTO.class);

        assertThat(detailedOrder.id()).isEqualTo(mockedOrder.getId());
        assertThat(detailedOrder.orderNumber()).isEqualTo(mockedOrder.getOrderNumber());
        assertThat(detailedOrder.orderItems()).hasSize(mockedOrder.getOrderItems().size());
        assertThat(detailedOrder.orderDiscount()).isEqualTo(mockedOrder.getOrderDiscount());
        assertThat(detailedOrder.totalOrder()).isEqualTo(mockedOrder.getTotalOrder());
        assertThat(detailedOrder.orderStatus()).isEqualTo(mockedOrder.getOrderStatus());
    }


    @Test
    @DisplayName("Discount: Should return HTTP 200 when the input data are valid")
    void applyDiscount_second_scenario() throws Exception {
        UUID orderId = UUID.randomUUID();

        DiscountOrderDTO discountData = new DiscountOrderDTO(
                BigDecimal.valueOf(10),
                null,
                DiscountOrigin.COUPON
        );
        Order mockedOrder = Mockito.mock(Order.class);

        when(update.applyDiscount(any(), any())).thenReturn(mockedOrder);

        MvcResult result = mvc.perform(put("/api/orders/discount/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(discountData))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockedOrder.getId()))
                .andExpect(jsonPath("$.orderNumber").value(mockedOrder.getOrderNumber()))
                .andExpect(jsonPath("$.orderItems").isArray())
                .andExpect(jsonPath("$.orderDiscount").value(mockedOrder.getOrderDiscount()))
                .andExpect(jsonPath("$.totalOrder").value(mockedOrder.getTotalOrder()))
                .andExpect(jsonPath("$.orderStatus").value(mockedOrder.getOrderStatus()))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        DetailOrderDTO detailedOrder = objectMapper.readValue(responseBody, DetailOrderDTO.class);

        assertThat(detailedOrder.id()).isEqualTo(mockedOrder.getId());
        assertThat(detailedOrder.orderNumber()).isEqualTo(mockedOrder.getOrderNumber());
        assertThat(detailedOrder.orderItems()).hasSize(mockedOrder.getOrderItems().size());
        assertThat(detailedOrder.orderDiscount()).isEqualTo(mockedOrder.getOrderDiscount());
        assertThat(detailedOrder.totalOrder()).isEqualTo(mockedOrder.getTotalOrder());
        assertThat(detailedOrder.orderStatus()).isEqualTo(mockedOrder.getOrderStatus());
    }


    @Test
    @DisplayName("Modify: Should return HTTP 200 when the input data are valid")
    void modifyQuantity_second_scenario() throws Exception{
        UUID orderId = UUID.randomUUID();

        UpdateOrderItemDTO updateData = new UpdateOrderItemDTO(
                10
        );
        OrderItem mockedOrderItem = Mockito.mock(OrderItem.class);

        when(orderItemService.modifyQuantity(any(), any())).thenReturn(mockedOrderItem);

        MvcResult result = mvc.perform(put("/api/orders/orderItem/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockedOrderItem.getId()))
                .andExpect(jsonPath("$.product").value(mockedOrderItem.getProduct()))
                .andExpect(jsonPath("$.quantity").value(mockedOrderItem.getQuantity()))
                .andExpect(jsonPath("$.totalPrice").value(mockedOrderItem.getTotalPrice()))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        DetailOrderItem detailedOrderItem = objectMapper.readValue(responseBody, DetailOrderItem.class);

        DetailProductDTO detailedProductDTO = ProductMapper.INSTANCE.toDetailProduct(mockedOrderItem.getProduct());

        assertThat(detailedOrderItem.id()).isEqualTo(mockedOrderItem.getId());
        assertThat(detailedOrderItem.product()).isEqualTo(detailedProductDTO);
        assertThat(detailedOrderItem.quantity()).isEqualTo(mockedOrderItem.getQuantity());
        assertThat(detailedOrderItem.totalPrice()).isEqualTo(mockedOrderItem.getTotalPrice());
    }


    @Test
    @DisplayName("Remove: Should return HTTP 200 when the input data are valid")
    void removeOrderItem_second_scenario() throws Exception{
        UUID orderId = UUID.randomUUID();

        UUID orderItemUUID = UUID.randomUUID();

        mvc.perform(put("/api/orders/orderItem/remove/{orderUUID}", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderItemUUID)))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("getBadRequests")
    @DisplayName("Should return HTTP 400 when the input data are invalid")
    void badRequest(MockHttpServletRequestBuilder httpMethod) throws Exception {
        var response = mvc
                .perform(httpMethod)
                .andReturn().getResponse();

        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static Stream<MockHttpServletRequestBuilder> getBadRequests() {
        UUID orderId = UUID.randomUUID();

        return Stream.of(
                post("/api/orders"),
                put("/api/orders/orderItem/remove/{orderUUID}", orderId),
                put("/api/orders/orderItem/{id}", orderId),
                put("/api/orders/discount/{id}", orderId),
                post("/api/orders/{id}", orderId)
        );
    }

    @Test
    void list() {
    }

    @Test
    void detail() {
    }
}