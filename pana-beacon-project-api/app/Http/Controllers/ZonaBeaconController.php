<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;


class ZonaBeaconController extends Controller
{
    public function evento(Request $request)
    {
        $user = $request->user();
        $hora = $request['hora'];

        Log::info("El usuario $user->name está: {$request['evento']}. hora de registro en App $hora");

        return response()->json([
            'message' => "Evento registrado"
        ]);
    }
}
