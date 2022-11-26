package com.example.everypractice.prinoptions.movies.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.everypractice.consval.CURRENT_USER_NAME
import com.example.everypractice.databinding.FragmentInitialBinding
import com.example.everypractice.prinoptions.HistoryApplication
import com.example.everypractice.prinoptions.movies.recycler.page.AdapterViewPageInitialRecommended
import com.example.everypractice.prinoptions.movies.vm.FavouriteMoviesViewModelFactory
import com.example.everypractice.prinoptions.movies.vm.MovieViewModel
import com.example.everypractice.start.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.abs


class InitialFragment : Fragment() {

    private var _binding: FragmentInitialBinding? = null
    private val binding get() = _binding!!

    //VIEW-PAGER-2
    private lateinit var viewPager2: ViewPager2

    private val sharedViewModel: MovieViewModel by activityViewModels {
        FavouriteMoviesViewModelFactory(
            (requireActivity().application as HistoryApplication).movieRepository
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInitialBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCustom()

        binding.btnNotification.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onOutSession()
        }

        viewPager2 = binding.vp2DailyRecomendation
        lifecycleScope.launchWhenCreated {
            sharedViewModel.get4FavouriteRecommendedMovies().collectLatest {
                viewPager2.adapter = AdapterViewPageInitialRecommended(it)
            }
        }
        viewPager2.offscreenPageLimit = 3
        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.getChildAt(0).overScrollMode = 2
        viewPager2.post {
            viewPager2.setCurrentItem(1, true)
        }

        setUpoTransformer()

/*
        //ELEMENTO INICIO EJEMPLO
        sharedViewModel.temporaryFavouriteMovies.observe(viewLifecycleOwner){
            binding.movieName.text=it.results[0].originalTitle
            //Glide.with(this).load(it.results[0].posterUrl).into(binding.portada)
            //SETEAR BIEN AQUI EL VALOR DE DIFUMINADO, MENOS VALOS MAS DIFU.... POR DEFECTO SE LE PONE EN [2,2]
            *//*Glide.with(this).asBitmap().load(it.results[0].posterUrl).into(object : CustomTarget<Bitmap>(50,50){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.glass.setImageDrawable(BitmapDrawable(binding.glass.rootView.context.resources,resource))
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    binding.glass.setImageDrawable(null)
                }
            })*//*
            binding.portada.load(it.results[0].posterUrl){
                listener(
                    onSuccess = { _, _ ->
                        Toast.makeText(requireContext(), "Success Download image", Toast.LENGTH_SHORT).show()
                    },
                    onError = { request: ImageRequest, throwable: Throwable ->
                        request.error
                        Toast.makeText(requireContext(), "$throwable", Toast.LENGTH_SHORT).show()
                    }
                )
                error(R.drawable.sticker_incorrect)
            }
            binding.glass.load(it.results[0].posterUrl){
                    crossfade(750)
                        .transformations(
                            BlurTransformation(requireContext(), radius = 18f)
                        )
                        .build()

            }*/
    }

    private fun setupCustom() {
        binding.tvPerfilName.text = CURRENT_USER_NAME
            //FirebaseAuth.getInstance().currentUser?.email.toString()
    }

    private fun onOutSession() {

        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)

    }

    /**
     * Function that make a format to the elements of viewPager
     * */
    private fun setUpoTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f

            when{
                position < -1 ->{
                    //izq doble
                    page.y = -position*200f
                }

                position <0 ->{
                    //center to izq
                    page.rotation = position*90/5
                    page.y = -position*200f

                }

                position == 0f ->{
                    //centro
                    page.rotation = 0f
                    page.y = 0f
                }
                position <= 1 ->{
                    //derecha to center
                    page.rotation = position*90/5
                    page.y = 200f*position


                }
                else -> {
                    //doble derecha
                    page.rotation=position*90/5
                    page.y = 200f*position
                }

            }

        }

        viewPager2.setPageTransformer(transformer)
    }

}


