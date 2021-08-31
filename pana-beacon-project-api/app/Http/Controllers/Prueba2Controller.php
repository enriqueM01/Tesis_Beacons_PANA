<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class Prueba2Controller extends Controller
{
    public function index(){
      //return "PRUEBA PRUEBA";
      return view('welcome');
    }
    public function create(){
      return "Esta es una prueba con tesis/curso SIN VARIABLES";
    }
    public function show($a){
      return "esta es la var: $a";
    }
};
