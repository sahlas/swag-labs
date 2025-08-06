Feature: Product Catalog
  As a customer,
  I want to easily search, filter and sort products in the catalog
  So that I can find the products I am interested in quickly and efficiently.

  Sally is an online shopper who wants to choose products to purchase in the catalog.

  Rule: Customers can view individual product details on the inventory-details page.

    Background:
      Given Sally logs in

    Scenario: Sally views product details for a specific product
      When Sally views the details of the "Sauce Labs Backpack" by clicking on the product name
      Then the product details page should display the following information
        | product_name        | description                                                                                                                            | price  |
        | Sauce Labs Backpack | carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection. | $29.99 |
      And the product image for "Sauce Labs Backpack" should be displayed

  Rule: Customers should be able to cancel the checkout process from multiple phases of the check out process.

    Background:
      Given Sally logs in

    Scenario: Sally cancels the checkout process from the checkout step 1 page
      When Sally adds the following products to the cart
        | product                 |
        | Sauce Labs Backpack     |
        | Sauce Labs Bolt T-Shirt |
      And Sally views her cart
      And Sally begins the checkout process
      Then Sally continues to the overview page for review only to cancel the order

    Scenario: Sally cancels the checkout process from the checkout step 2 page
      When Sally adds the following products to the cart
        | product                           |
        | Sauce Labs Fleece Jacket          |
        | Test.allTheThings() T-Shirt (Red) |
      And Sally views her cart
      And Sally begins the checkout process
      And Sally fills in her personal information
        | first_name | last_name | postal_code |
        | Sally      | Shopper   | 12345       |
      Then Sally continues to the information page for review only to cancel the order

  Rule: When a customer is ready to move on they will review their shopping cart.

    Background:
      Given Sally logs in
      And Sally adds the following products to the cart
        | product                           |
        | Sauce Labs Backpack               |
        | Sauce Labs Bolt T-Shirt           |
        | Sauce Labs Fleece Jacket          |
        | Test.allTheThings() T-Shirt (Red) |
        | Sauce Labs Onesie                 |
      When Sally views her cart
      Then the shopping cart page should indicate all products picked for checkout
        | product                           | quantity | total  |
        | Sauce Labs Backpack               | 1        | $29.99 |
        | Sauce Labs Bolt T-Shirt           | 1        | $15.99 |
        | Sauce Labs Fleece Jacket          | 1        | $49.99 |
        | Test.allTheThings() T-Shirt (Red) | 1        | $15.99 |
        | Sauce Labs Onesie                 | 1        | $7.99  |

    Scenario: Sally proceeds to checkout with items in her cart and completes the order
      When Sally begins the checkout process
      Then Sally fills in her personal information
        | first_name | last_name | postal_code |
        | Sally      | Shopper   | 12345       |
      And Sally continues to the overview page for review
        | product                           | quantity | total  |
        | Sauce Labs Backpack               | 1        | $29.99 |
        | Sauce Labs Bolt T-Shirt           | 1        | $15.99 |
        | Sauce Labs Fleece Jacket          | 1        | $49.99 |
        | Test.allTheThings() T-Shirt (Red) | 1        | $15.99 |
        | Sauce Labs Onesie                 | 1        | $7.99  |
      When Sally clicks on the finish button
      Then she should see the confirmation message

    Scenario: Sally modifies her cart and proceeds to checkout then completes the order

      When Sally removes the "Sauce Labs Backpack" from her cart
      And Sally begins the checkout process
      Then Sally fills in her personal information
        | first_name | last_name | postal_code |
        | Sally      | Shopper   | 12345       |
      And Sally continues to the overview page for review
        | product                           | quantity | total  |
        | Sauce Labs Bolt T-Shirt           | 1        | $15.99 |
        | Sauce Labs Fleece Jacket          | 1        | $49.99 |
        | Test.allTheThings() T-Shirt (Red) | 1        | $15.99 |
        | Sauce Labs Onesie                 | 1        | $7.99  |
      When Sally clicks on the finish button
      Then she should see the confirmation message

  Rule: Customers should be able to add products to their shopping cart from the inventory page.
    Background:
      Given Sally logs in

    Scenario Outline: Sally adds a product to her cart
      When Sally adds a <productName> to her cart
      Then the "Sauce Labs Backpack" status indicates it has been added to the cart
      Examples:
        | productName           |
        | "Sauce Labs Backpack" |

    Scenario: Sally adds multiple products to the shopping cart
      When Sally adds the following products to the cart
        | product                           |
        | Sauce Labs Backpack               |
        | Test.allTheThings() T-Shirt (Red) |
      Then Sally views her cart
      And the shopping cart page should indicate all products picked for checkout
        | product                           | quantity | total  |
        | Sauce Labs Backpack               | 1        | $29.99 |
        | Test.allTheThings() T-Shirt (Red) | 1        | $15.99 |

  Rule: Customers should be able to continue shopping after adding products to their cart.

    Background:
      Given Sally logs in
      And Sally adds the following products to the cart
        | product                           |
        | Sauce Labs Backpack               |
        | Test.allTheThings() T-Shirt (Red) |

    Scenario: Sally continues shopping after adding products to her cart
      When Sally continues shopping after adding products to her cart
      Then she should be able to view the inventory page and add more products


  Rule: Customers should be able to remove items from their cart while on the main inventory page.
    Background:
      Given Sally logs in

    Scenario Outline: Sally removes products from her cart
      When Sally adds the following products to the cart
        | product                           |
        | Sauce Labs Backpack               |
        | Test.allTheThings() T-Shirt (Red) |
      And Sally removes the "<product>" from her cart on inventory page
      When Sally views her cart
      Then check that only the expected products are in her cart

      Examples: (product)
        | product                           |
        | Test.allTheThings() T-Shirt (Red) |

    Scenario: Sally modifies her cart by removing all products

      When Sally adds the following products to the cart
        | product                           |
        | Sauce Labs Backpack               |
        | Sauce Labs Bolt T-Shirt           |
        | Sauce Labs Fleece Jacket          |
        | Test.allTheThings() T-Shirt (Red) |
        | Sauce Labs Onesie                 |
      And Sally removes all products from her cart
        | product                           |
        | Sauce Labs Backpack               |
        | Sauce Labs Bolt T-Shirt           |
        | Sauce Labs Fleece Jacket          |
        | Test.allTheThings() T-Shirt (Red) |
        | Sauce Labs Onesie                 |
      Then Sally views her cart and checks that all the products have been removed


  Rule: Customers should be able to view their cart and remove items while on the shopping cart page.
    Background:
      Given Sally logs in
      And Sally adds the following products to the cart
        | product                           |
        | Sauce Labs Backpack               |
        | Sauce Labs Bolt T-Shirt           |
        | Sauce Labs Fleece Jacket          |
        | Test.allTheThings() T-Shirt (Red) |
        | Sauce Labs Onesie                 |

    Scenario: Sally views her cart
      When Sally views her cart
      Then the shopping cart page should indicate all products picked for checkout
        | product                           | quantity | total  |
        | Sauce Labs Backpack               | 1        | $29.99 |
        | Test.allTheThings() T-Shirt (Red) | 1        | $15.99 |
        | Sauce Labs Bolt T-Shirt           | 1        | $15.99 |
        | Sauce Labs Fleece Jacket          | 1        | $49.99 |
        | Sauce Labs Onesie                 | 1        | $7.99  |


  Rule: Customers should be able to sort the inventory items by various criteria.
    Background:
      Given Sally logs in

    Scenario Outline: Sally sorts by different criteria after searching for a product
      When Sally sorts by "<sort_criteria>"
      Then the first product displayed should be "<first_product>"
      Examples: (sort criteria)
        | sort_criteria       | first_product                     |
        | Price (low to high) | Sauce Labs Onesie                 |
        | Price (high to low) | Sauce Labs Fleece Jacket          |
        | Name (A to Z)       | Sauce Labs Backpack               |
        | Name (Z to A)       | Test.allTheThings() T-Shirt (Red) |

  Rule: Attempts at passing in false or incorrect credentials should result in error message with an explanation.

    Scenario Outline: Sally tries to log in with invalid credentials
      Given Sally is on the login page
      When Sally enters her "<username>" and "<password> that are invalid"
      Then the error message should be "<error_message>"
      Examples: (invalid login)
        | username      | password            | error_message                                               |
        | standard_user | not_so_secret_sauce | Username and password do not match any user in this service |
        |               |                     | Username is required                                        |
        | standard_user |                     | Username and password do not match any user in this service |
        |               | secret_sauce        | Username is required                                        |
    Scenario Outline: Sally tries to access the inventory page without logging in
      Given Sally opens a browser link to the inventory page
      When Sally is redirected to the login page
      Then the error message should be "<error_message>"
      Examples: (invalid access)
        | error_message                                                 |
        | You can only access '/inventory.html' when you are logged in. |
