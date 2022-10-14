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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import seino.indomobil.dmsmobile.databinding.ItemOnboardingBinding
import seino.indomobil.dmsmobile.domain.core.onboarding.OnboardingEntity

@AndroidEntryPoint
class OnBoardingSlideAdapter() :
    RecyclerView.Adapter<OnBoardingSlideAdapter.ViewHolder>() {

    private val datas = ArrayList<OnboardingEntity>()

    fun setListData(listData: List<OnboardingEntity>) {
        datas.clear()
        datas.addAll(listData)
        notifyDataSetChanged()
    }

    inner class ViewHolder(var binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: OnboardingEntity) {
            binding.data = data
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemOnboardingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])

    }

    override fun getItemCount(): Int = datas.size
}