<?php
define('HOST','localhost');
define('USER','root');
define('PASS','lol');
define('DB','shopdroid');
$con = mysqli_connect(HOST,USER,PASS,DB);
//$sql = "SELECT * FROM products WHERE state ='haryana' and city='panipat'";
$sql = "SELECT * FROM products";
$res = mysqli_query($con,$sql);
$result = array();
while($row = mysqli_fetch_array($res)){
array_push($result,
array('product_name'=>$row[3],
'category'=>$row[4],
'price'=>$row[7],
'stock'=>$row[6],
'shop'=>$row[9]
));
}
echo json_encode(array("result"=>$result));
mysqli_close($con);
?>