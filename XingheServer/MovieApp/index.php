<?php
/*
Author: Xinghe Lu
Date: 3/11/2016
Purpose: This file will deal with the parsing of url and call corresponding function upon the definition of the RESTul url
Version 1.0
*/

require '../../vendor/autoload.php';
require 'db.php';
require 'movies.php';


$app = new \Slim\App;

$app->get(//testing
	'/',
	function (){
		//sayHi();
		sayHiMovie();
	}
	
);

$app->get(//get movies
	'/movies/',
	function(){
	  //echo "<h1>hi from /movies/</h1>";
	  getMovies();
	}
);

$app->get(//get movies having rating higher than the float number specified by the user
	'/movies/rating/{mid}',
	function($request, $response, $args){
	  //echo "hi, ".$args['mid'];
	  getMoviesAboveRating($args['mid']);
	}
);

$app->get(//get movie details according to the specific movieID inluding at the end of the url
	'/movies/id/{mid}',
	function($request, $response, $args){
	  //echo "hi, ".$args['mid'];
	  getMoviesDetail($args['mid']);
	}
);

/*$app->get(//get user input and delete the data, will redirect to http://xinghe.com/post/delete
	'/delete',
	function($request, $response, $args){
		echo "<form action=\"http://xinghe.com/post/delete\" method=\"post\">";
		echo"<h1>Please enter the ID of the movie you would like to delete</h1>";
		echo "ID: <input type=\"text\" name=\"id\"><br>";
		echo "<input type=\"submit\" value=\"Submit\">";
		echo "</form>";
	}
);*/

$app->post(//get user input and call the deleteMovie() method to delete the movie from the database
	'/post/delete', 
	function($request, $response, $args){
		
		$data = json_decode($request->getBody(), true);
		deleteMovie($data['id']);
  }
);

/*$app->get(//get user input and insert the new movie data and redirect to "http://xinghe.com/post/add
	'/add',
	function($request, $response, $args){
		echo "<form action=\"http://xinghe.com/post/add\" method=\"post\">";
		echo"<h1>Please enter the info of the movie you would add to the database</h1>";
		echo "ID: <input type=\"text\" name=\"id\"><br>";
		echo "Name: <input type=\"text\" name=\"name\"><br>";
		echo "Description: <input type=\"text\" name=\"description\"><br>";
		echo "Stars: <input type=\"text\" name=\"stars\"><br>";
		echo "Length: <input type=\"text\" name=\"length\"><br>";
		echo "Image: <input type=\"text\" name=\"image\"><br>";
		echo "Year: <input type=\"text\" name=\"year\"><br>";
		echo "rating: <input type=\"text\" name=\"rating\"><br>";
		echo "Director: <input type=\"text\" name=\"director\"><br>";
		echo "Url: <input type=\"text\" name=\"url\"><br>";
		echo "<input type=\"submit\" value=\"Submit\">";
		echo "</form>";
	}
);*/

$app->post(//pass the data collected from the user to method addMovie() to insert the new movie data to the database
	'/post/add', 
	function($request, $response, $args){
		
		//$request->getParsedBody();
		$data = json_decode($request->getBody(), true);
		addMovie($data['id'],$data['name'],$data['description'],$data['stars'],$data['length'],$data['image'],(int)$data['year']
		,(float)$data['rating'],$data['director'],$data['url']);
  }
);


$app->run();
?>