<?php

namespace App\Http\Controllers;

use Illuminate\Support\Facades\Hash;
use Illuminate\Http\Request;
use App\Models\User;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Log;

class AuthController extends Controller
{
    

    
    public function register(Request $request)
    {   
        $validatedData = $request->validate([
            'name' => 'required|string|max:255',
                   'email' => 'required|string|email|max:255|unique:users',
                   'password' => 'required|string|min:8',
        ]);

      $user = User::create([
              'name' => $validatedData['name'],
                   'email' => $validatedData['email'],
                   'password' => Hash::make($validatedData['password']),
       ]);

        $token = $user->createToken('auth_token')->plainTextToken;

        return response()->json([
              'access_token' => $token,
                   'token_type' => 'Bearer',
        ]);
}

    public function login(Request $request)
    {
        if (!Auth::attempt($request->only('email', 'password'))) {
            return response()->json([
            'message' => 'Invalid login details'
            ], 401);
        }

        $user = User::where('email', $request['email'])->firstOrFail();

        $token = $user->createToken('auth_token')->plainTextToken;

        $hora = $request['hora'];

        Log::info("$user->name inició sesión. Hora de registro App $hora");

        return response()->json([
           'access_token' => $token,
           'token_type' => 'Bearer',
           'message' => "Inicio de sesión registrado"
        ]);
    }


    public function me(Request $request)
    {   
        return $request->user();
    }


    public function logout(Request $request)
    {
        $user = $request->user();
        $hora = $request['hora'];

        Log::info("$user->name ha cerrado sesión. Hora registro en App $hora");

        return response()->json([
            'message' => "Cierre de sesión registrado"
        ]);
    }

}
