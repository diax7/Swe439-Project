package com.salesmanager.shop.populator.order;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.catalog.product.ProductService;
import com.salesmanager.core.business.services.catalog.product.attribute.ProductAttributeService;
import com.salesmanager.core.business.services.shoppingcart.ShoppingCartService;
import com.salesmanager.core.business.utils.AbstractDataPopulator;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.attribute.ProductAttribute;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.shoppingcart.ShoppingCartItem;
import com.salesmanager.shop.model.order.PersistableOrderProduct;
import java.util.Objects;
import org.apache.commons.lang3.Validate;


public class ShoppingCartItemPopulator extends
    AbstractDataPopulator<PersistableOrderProduct, ShoppingCartItem> {

  private ProductService productService;

  private ProductAttributeService productAttributeService;

  private ShoppingCartService shoppingCartService;

  @Override
  public ShoppingCartItem populate(PersistableOrderProduct source, ShoppingCartItem target,
      MerchantStore store, Language language) throws ServiceException {
    Validate.notNull(productService, "Requires to set productService");
    Validate.notNull(productAttributeService, "Requires to set productAttributeService");
    Validate.notNull(shoppingCartService, "Requires to set shoppingCartService");

    Product product = productService.getById(source.getProduct().getId());
    if (source.getAttributes() != null) {

      for (com.salesmanager.shop.model.catalog.product.attribute.ProductAttribute attr : source
          .getAttributes()) {
        ProductAttribute attribute = productAttributeService.getById(attr.getId());

        if (attribute != null && !Objects
            .equals(attribute.getProduct().getId(), source.getProduct().getId())) {
          throw new RuntimeException(
              "ProductAttribute with id " + attr.getId() + " is not assigned to Product id "
                  + source.getProduct().getId());
        }
        product.getAttributes().add(attribute);
      }
    }

    return shoppingCartService.populateShoppingCartItem(product);
  }

  @Override
  protected ShoppingCartItem createTarget() {
    // TODO Auto-generated method stub
    return null;
  }

  public ProductAttributeService getProductAttributeService() {
    return productAttributeService;
  }

  public void setProductAttributeService(ProductAttributeService productAttributeService) {
    this.productAttributeService = productAttributeService;
  }

  public ProductService getProductService() {
    return productService;
  }

  public void setProductService(ProductService productService) {
    this.productService = productService;
  }

  public ShoppingCartService getShoppingCartService() {
    return shoppingCartService;
  }

  public void setShoppingCartService(ShoppingCartService shoppingCartService) {
    this.shoppingCartService = shoppingCartService;
  }
}
