<?php

namespace App\Http\Controllers;

use App\Http\Requests\TicketPaymentUpdateReq;
use App\Http\Requests\TicketRequest;
use App\Models\Ticket;
use http\Env\Response;
use Illuminate\Http\Request;

class TicketAPI extends Controller
{
    public function create(TicketRequest $request) {
        try {
            Ticket::create([
                "channelId" => $request->channelId
            ]);
        } catch (\Exception $e) {
            return \response()->json([
               "status" => "ERROR - {$e}"
            ]);
        }
        return response()->json([
            "status" => "VALID"
        ]);
    }
    public function paymentUpdate(TicketPaymentUpdateReq $request){
        $ticket = Ticket::where("channelId", $request->channelId)->update(["payment_id" => $request->paymentId]);
        if(isset($ticket)) {
            $status = "VALID";
        } else {
            $status = "ERROR";
        }
        return response()->json([
            "status" => $status
        ]);
    }
    public function allTickets() {
        return response()->json([
            Ticket::all()
        ]);
    }
    public function getCertain(Request $request) {
        $certainTicket = Ticket::where("channelId", $request->get("channelId"))->first();
        return response()->json([
            $certainTicket
        ]);
    }
}
