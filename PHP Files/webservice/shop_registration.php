<?php
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
// array for JSON response
$response = array();
// check for required fields
if (isset($_POST['username']) && isset($_POST['password']) && isset($_POST['shop_name']) && isset($_POST['address']) && isset($_POST['state']) && isset($_POST['city']) && isset($_POST['pincode']) && isset($_POST['mobile_no'])) {
  	$username = $_POST['username'];
	$password = $_POST['password'];
	$shop_name = $_POST['shop_name'];
	$address = $_POST['address'];
	$state = $_POST['state'];
	$city = $_POST['city'];
	$pincode = $_POST['pincode'];
	$mobile_no = $_POST['mobile_no'];
	
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
    // connecting to db
    $db = new DB_CONNECT();
    // mysql inserting a new row
    $result = mysql_query("INSERT INTO shop_registration(username, password, shop_name, address, state, city, pincode, mobile_no) VALUES('$username', '$password', '$shop_name', '$address', '$state', '$city', '$pincode', '$mobile_no')");
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