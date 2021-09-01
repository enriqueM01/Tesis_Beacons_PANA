<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;


class ZonaBeaconController extends Controller
{
    public function evento(Request $request)
    {
        Log::info("$request->zona");

        return response()->json([
            'message' => "todo OK",

        ]);
    }
}
