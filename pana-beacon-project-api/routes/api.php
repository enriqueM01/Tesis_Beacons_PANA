<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\ServiceController;
use App\Http\Controllers\ZonaBeaconController;

Route::post('/tokens/create', function (Request $request) {
    $token = $request->user()->createToken($request->token_name);

    return ['token' => $token->plainTextToken];
});

Route::post('/register', [AuthController::class, 'register']);

Route::middleware('auth:api')->get('/user', function (Request $request) {
    return $request->user();
});

Route::post('/login', [AuthController::class, 'login']);


Route::middleware(['auth:sanctum'])->group(function (){
    
    Route::post('/me', [AuthController::class, 'me']);
    Route::post('/evento', [ZonaBeaconController::class, 'evento']);
    Route::post('/inicioservicio', [ServiceController::class, 'inicioservicio']);
    Route::post('/finservicio', [ServiceController::class, 'finservicio']);
    Route::post('/ayuda', [ServiceController::class, 'ayuda']);
    Route::post('/logout', [AuthController::class, 'logout']);
});
