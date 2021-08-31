<?php

namespace Database\Seeders;

use App\Models\User;
use Illuminate\Database\Seeder;

class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     *
     * @return void
     */
    public function run()
    {
        // \App\Models\User::factory(10)->create();
        $table = new User();
        $table->name="alberto a";
        $table->id="3";
        $table->password="aloalo";
        $table->email="email";

        $table->save();

        $table1 = new User();
        $table1->name="Paola Alvarez Alava";
        $table1->id="4";
        $table1->password="1234556788967645w";
        $table1->email="paoalvarez1612@gmail.com";

        $table1->save();


    }
}
