/*
 * *
 *    Created by Seino Developer on 10/13/22, 9:47 AM                           *
 *    Copyright (c) 2022 DevTeam - New DMS Mobile. All rights reserved.           *
 *                                                                                       *
 *    This project and associated documentation files are limited to be used,            *
 *    reproduced, distributed, copied, modified, merged, published, sublicensed,         *
 *    and/or sold to only authorized staff. Should you find yourself is unauthorized,    *
 *    please do not use this project and its associated documentation files in any       *
 *    kind of intentions and conditions.                                                 *
 *                                                                                       *
 *    In order to obtain access to use and involve in this project, you may proceed      *
 *    to inform the authorized staff. By using and involving in this project, you agree  *
 *    to follow our regulations, terms and conditions.                                   *
 *                                                                                       *
 *    This project and source code may use libraries or frameworks that are released     *
 *    under various Open-Source license. Use of those libraries and frameworks are       *
 *    governed by their own individual licenses.                                         *
 *                                                                                       *
 *    The use of this project and source code follows the guideline as described and     *
 *    explained on confluence seinoindomobil.co.id under DMS Mobile New Platform project.*
 *    Please always refer to the project space to follow the guideline.                  *
 *                                                                                       *
 *    If you have any question, please inform our staff or development leader.           *
 *
 */

package seino.indomobil.dmsmobile.presentation.ui.auth

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import seino.indomobil.dmsmobile.R
import seino.indomobil.dmsmobile.databinding.FragmentOnBoardSlideBinding
import seino.indomobil.dmsmobile.domain.core.onboarding.generateDataOnboarding
import seino.indomobil.dmsmobile.presentation.utils.BaseFragment

@AndroidEntryPoint
class OnBoardSlideFragment : BaseFragment<FragmentOnBoardSlideBinding>(
    FragmentOnBoardSlideBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnPreviousStep.setOnClickListener {
                if (getItem() == 0) activity?.finish()
                else binding.viewPager2.setCurrentItem(getItem() - 1, true)
            }

            btnNextStep.setOnClickListener {
                if (getItem() > binding.viewPager2.childCount) activity?.finish()
                else binding.viewPager2.setCurrentItem(getItem() + 1, true)
            }
        }

        binding.apply {
            val onBoardAdapter = OnBoardingSlideAdapter()
            onBoardAdapter.setListData(requireContext().generateDataOnboarding())

            viewPager2.adapter = onBoardAdapter
            viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            indicator.setViewPager(viewPager2)

            binding.viewPager2.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when (position) {
                        0 -> {
                            btnPreviousStep.visibility = View.GONE
                            btnNextStep.visibility = View.VISIBLE
                        }
                        1 -> {
                            btnPreviousStep.visibility = View.VISIBLE
                            btnNextStep.visibility = View.VISIBLE
                            btnNextStep.text = getString(R.string.lanjut)

                        }
                        2 -> {
                            btnPreviousStep.visibility = View.VISIBLE
                            btnNextStep.visibility = View.VISIBLE
                            btnNextStep.text = getString(R.string.mulai)
                            btnNextStep.setOnClickListener {
                                findNavController().navigate(R.id.action_onBoardSlideFragment_to_loginFragment)
                            }
                        }
                    }
                }
            })
        }
    }

    private fun getItem(): Int {
        return binding.viewPager2.currentItem
    }
}