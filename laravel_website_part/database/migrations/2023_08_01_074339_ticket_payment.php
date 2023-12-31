<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create("tickets", function(Blueprint $table) {
            $table->id();
            $table->string("channelId")->unique();
            $table->unsignedBigInteger("payment_id")->nullable();
            $table->foreign("payment_id")->references("id")->on("payments");
            $table->string("ticket_status")->default("payment");
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists("tickets");
    }
};
