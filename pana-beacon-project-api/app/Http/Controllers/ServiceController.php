<?php
namespace App\Http\Controllers;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;
class ServiceController extends Controller
{
    public function inicioservicio(Request $request)
    {
        $user = $request->user();
        $hora = $request['hora'];

        Log::info("El usuario $user->name inicia servicio. Hora de registro en App $hora");

        return response()->json([
            'message' => "Inicio de servicio registrado"
        ]);
    }
    public function finservicio(request $request)
    {
        $user = $request->user();
        $hora = $request['hora'];

        Log::info("El usuario $user->name finaliza servicio. Hora de registro en App $hora");

        return response()->json([
            'message'=> "Fin de servicio registrado"
        ]);

    }
    public function ayuda(request $request)
    {
        $user = $request->user();
        $hora = $request['hora'];

        Log::info("El usuario $user->name solicita ayuda. hora de registro en App $hora");

        return response()->json([
            'message'=> "Ayuda notificada al Centro de Control"
        ]);
    }


}

