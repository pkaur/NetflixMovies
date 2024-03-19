package com.puneetkaur.movies

import MovieFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.puneetkaur.movies.adapter.MovieAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val movieFragment = MovieFragment()
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.container, movieFragment,
                MovieFragment.TAG
            )
            .commit()
    }
}