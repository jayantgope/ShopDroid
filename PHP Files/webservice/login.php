<?php
	$response = array();
    if (isset($_POST['username']) && isset($_POST['password']))
	{
		$username = $_POST['username'];
		$password = $_POST['password'];
		// include db connect class
		require_once'/db_connect.php';
		// connecting to db
		$db = new DB_CONNECT();
		//$sql = "SELECT * FROM login WHERE username = '".$username."' AND password = '".$password."'";
		$sql = "SELECT * FROM login";
		// WHERE username = '$username' and password = '$password'";
		//$sql = "SELECT * FROM login WHERE username = '".$username."' AND password = '".$password."'";
		if(mysql_num_rows($result))
		{
			$response["success"] = 1;
        	$response["message"] = "Success";
        	// echoing JSON response
        	echo json_encode($response);
    	} 
		else 
		{
			// failed to insert row
			$response["success"] = 0;
			$response["message"] = "Fail";
		  
			// echoing JSON response
			echo json_encode($response);
		}
	}
	else 
	{
		// required field is missing
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing";
		// echoing JSON response
		echo json_encode($response);
	}
?>

