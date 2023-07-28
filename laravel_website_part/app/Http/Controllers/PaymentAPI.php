<?php

namespace App\Http\Controllers;

use App\Http\Requests\CreateRequest;
use App\Models\payment;
use Illuminate\Http\Request;
use Mockery\Exception;
use Nette\Utils\Random;
use PHPUnit\Framework\Error;

class PaymentAPI extends Controller
{
    public function create(CreateRequest $request) {
        $paymentId = Random::generate(10, '0-9a-z');

        try{
            $result = payment::create([
                "payment_id" => $paymentId,
                "payment_name" => $request->payment_name,
                "payment_price" => $request->payment_price
            ]);
            $status = "VALID";
        } catch (Exception $exception) {
            if ($exception instanceof \Illuminate\Database\QueryException && $exception->errorInfo[1] === 1062) {
                $status = "ERROR; name";
            } else {
                $status = "ERROR: " . $exception->getMessage();
            }
        }
        return response()->json([
            'status' => $status,
            'paymend_id' => $paymentId
        ]);
    }
}
