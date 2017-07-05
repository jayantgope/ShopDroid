<?php
define('HOST','localhost');
define('USER','root');
define('PASS','lol');
define('DB','shopdroid');
$con = mysqli_connect(HOST,USER,PASS,DB);
//$sql = "SELECT * FROM shop_registration WHERE state ='haryana' and city='panipat'";
$sql = "SELECT * FROM shop_registration";
$res = mysqli_query($con,$sql);
$result = array();
while($row = mysqli_fetch_array($res)){
array_push($result,
array('shopname'=>$row[0],
'username'=>$row[0],
'state'=>$row[4],
'city'=>$row[5]
));
}
echo json_encode(array("result"=>$result));
mysqli_close($con);
?>