<?php

    $queries = array();
    parse_str($_SERVER['QUERY_STRING'], $queries);
    $web=$queries['url'];


$curl = curl_init();

curl_setopt_array($curl, [
	CURLOPT_URL => "https://getvideo.p.rapidapi.com/?url=".$web,
	CURLOPT_RETURNTRANSFER => true,
	CURLOPT_FOLLOWLOCATION => true,
	CURLOPT_ENCODING => "",
	CURLOPT_MAXREDIRS => 10,
	CURLOPT_TIMEOUT => 30,
	CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
	CURLOPT_CUSTOMREQUEST => "GET",
	CURLOPT_HTTPHEADER => [
		"x-rapidapi-host: getvideo.p.rapidapi.com",
		"x-rapidapi-key: Here enter your own api key"  // here we use the "get video and audio api" from rapid api and redirect http request from the app to our own server then to rapid api to mesure the api call
	],
]);

$response = curl_exec($curl);
$err = curl_error($curl);

curl_close($curl);

if ($err) {
	echo "cURL Error #:" . $err;
} else {
	echo $response;
}