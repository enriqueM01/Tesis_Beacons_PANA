<?php

namespace App\Exceptions;

use Facade\FlareClient\Http\Response;
use Illuminate\Auth\AuthenticationException;
use Illuminate\Foundation\Exceptions\Handler as ExceptionHandler;
use Illuminate\Http\Response as HttpResponse;
use Illuminate\Support\Facades\Log;
use Throwable;

class Handler extends ExceptionHandler
{
    /**
     * A list of the exception types that are not reported.
     *
     * @var array
     */
    protected $dontReport = [
        //
    ];

    /**
     * A list of the inputs that are never flashed for validation exceptions.
     *
     * @var array
     */
    protected $dontFlash = [
        'current_password',
        'password',
        'password_confirmation',
    ];

    /**
     * Register the exception handling callbacks for the application.
     *
     * @return void
     */
    public function register()
    {
        $this->reportable(function (Throwable $e) {
            //
        });

    }
    public function render($request, Throwable $exception){
        Log::info('AAA', [$exception]);
        if ($exception instanceof AuthenticationException) {
            return response()->json([
                'message' => 'No autenticado',
                'data' => [],
            ], HttpResponse::HTTP_UNAUTHORIZED);
        }
    }

}
