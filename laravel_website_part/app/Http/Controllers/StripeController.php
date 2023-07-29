<?php

namespace App\Http\Controllers;

use App\Models\Payment;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Session;
use Stripe\Stripe;

class StripeController extends Controller
{
    public function redirect($id)
    {
        $allInfo = payment::where('payment_id', $id)->first();
        $stripe = new \Stripe\StripeClient(env("STRIPE_SECRET"));

        $checkout_session = $stripe->checkout->sessions->create([
            'line_items' => [[
                'price_data' => [
                    'currency' => 'usd',
                    'product_data' => [
                        'name' => $allInfo->payment_name,
                    ],
                    'unit_amount' => $allInfo->payment_price * 100,
                ],
                'quantity' => 1,
            ]],
            'mode' => 'payment',
            'success_url' => 'http://127.0.0.1:8000/success',
            'cancel_url' => 'http://127.0.0.1:8000/cancel',
        ]);

        header("HTTP/1.1 303 See Other");
        return redirect()->to($checkout_session->url);
    }
}
