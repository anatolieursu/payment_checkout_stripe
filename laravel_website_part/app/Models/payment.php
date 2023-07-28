<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class payment extends Model
{
    use HasFactory;
    protected $table = 'payments';
    protected $fillable = ["payment_id", "payment_name", "payment_price", "user"];
}
