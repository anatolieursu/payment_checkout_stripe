<?php

use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/', function () {
    return view('welcome');
});
Route::get('/payment/{id}', [\App\Http\Controllers\StripeController::class, 'redirect']);
Route::get("succes", [\App\Http\Controllers\StripeController::class, 'succes'])->name("checkout.succes");
Route::get("cancel", [\App\Http\Controllers\StripeController::class, 'cancel'])->name("checkout.cancel");
