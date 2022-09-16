package com.cmontero.tallerdpmkotlin

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.cmontero.tallerdpmkotlin.databinding.ActivityMainBinding
import com.cmontero.tallerdpmkotlin.databinding.NavHeaderMainBinding
import com.cmontero.tallerdpmkotlin.utils.Constantes
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)


       /* val bindingNav = NavHeaderMainBinding.inflate(layoutInflater)
        bindingNav.txtNameUser.text = "Juan Perez"*/



        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = findNavController(R.id.navHostFragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_basicos, R.id.nav_recyclerview, R.id.nav_usuarios, R.id.nav_cocteles, R.id.nav_lectorqr, R.id.nav_login, R.id.nav_mapas, R.id.nav_camara
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main, menu)
                val item = menu.findItem(R.id.action_addUser)
                item.isVisible = false
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_addUser -> {
                        val bundle = bundleOf(Constantes.MODO_EDICION to false, Constantes.OBJ_USUARIO to null)
                        navController.navigate(R.id.nav_newusuario, bundle)
                        true
                    }
                    else -> false
                }
            }

        })

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val user1 = navigationView.getHeaderView(0).findViewById<TextView>(R.id.txtNameUser1)
        val user2 = navigationView.getHeaderView(0).findViewById<TextView>(R.id.txtNameUser2)
        user1.text = "Bienvenido"
        user2.text = "Juan Perez"

    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}