<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\prueba1Controller;
use App\Http\Controllers\Prueba2Controller;

Route::get('/', [prueba1Controller::class,'__invoke']);

/*Route::get('/', function () {
    return view('welcome');
});*/

//Route::get('tesis', Prueba2Controller::class);

route::get('tesis', [Prueba2Controller::class,'index']);

route::get('tesis/curso', [Prueba2Controller::class,'create']);

route::get('tesis/{a}', [Prueba2Controller::class,'show']);

/*Route::get('tesis/{prueba}/{prueba2?}', function ($prueba, $prueba2 = null) {
    if($prueba2){
        return "VARIABLES tesis/$prueba/$prueba2";
    } else {
        return "VARIABLES no existe prueba2: tesis/$prueba/$prueba2";
    }

});*/