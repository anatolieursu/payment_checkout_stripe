<?php

namespace App\Http\Controllers;

use App\Models\Order;
use App\Models\Payment;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Session;
use Spatie\FlareClient\Http\Exceptions\NotFound;
use Stripe\Stripe;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;

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
            'success_url' => route("checkout.succes")."?session_id={CHECKOUT_SESSION_ID}",
            'cancel_url' => route("checkout.cancel"),
        ]);

        $order = new Order();
        $order->sessionID = $checkout_session->id;
        $order->payment_id = $allInfo->id;
        $order->price = $allInfo->payment_price;
        $order->status = "unpaid";
        $order->save();

        header("HTTP/1.1 303 See Other");
        return redirect()->to($checkout_session->url);
    }
    public function succes(Request $request) {
        \Stripe\Stripe::setApiKey(env("STRIPE_SECRET"));
        $sessionId = $request->get("session_id");

        try {
            $session = \Stripe\Checkout\Session::retrieve($sessionId);
            if(!$session) {
                throw new NotFoundHttpException;
            }
            $order = Order::where("sessionID", $session->id)->where("status", "unpaid")->get();
            if(!$order) {
                throw new NotFoundHttpException;
            }
            $order->status = "paid";
            $order->save();

            return view("succes_page");
        } catch (\Exception $e) {
            throw new NotFoundHttpException;
        }
    }
    public function cancel() {

    }
}
