<?php
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
// array for JSON response
$response = array();
// check for required fields
if (isset($_POST['product_code']) && isset($_POST['barcode']) && isset($_POST['product_name']) && isset($_POST['category']) && isset($_POST['location']) && isset($_POST['quantity']) && isset($_POST['unit_cost']) && isset($_POST['image']) && isset($_POST['shop_id'])&& isset($_POST['date_added'])) {
  	$product_code = $_POST['product_code'];
	$barcode = $_POST['barcode'];
	$product_name = $_POST['product_name'];
	$category = $_POST['category'];
	$location = $_POST['location'];
	$quantity = $_POST['quantity'];
	$unit_cost = $_POST['unit_cost'];
	$image = $_POST['image'];
	$shop_id = $_POST['shop_id'];
	$date_added = $_POST['date_added'];
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
    // connecting to db
    $db = new DB_CONNECT();
    // mysql inserting a new row
    $result = mysql_query("INSERT INTO products(product_code, barcode, product_name, category, location, quantity, unit_cost, image, shop_id, date_added) VALUES('$product_code', '$barcode', '$product_name', '$category', '$location', '$quantity', '$unit_cost', '$image', '$shop_id', '$date_added')");
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Success";
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Fail";
      
        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
    // echoing JSON response
    echo json_encode($response);
}
?>