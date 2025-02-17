package com.kotori316.fluidtank

import com.kotori316.fluidtank.tiles.Tiers
import net.minecraftforge.common.ForgeConfigSpec

object Config {
  val CATEGORY_RECIPE = "recipe"
  private var mContent: Content = _

  def content: Content = mContent

  def sync(): ForgeConfigSpec = {
    val builder = new ForgeConfigSpec.Builder
    mContent = new Content(builder)
    val spec = builder.build()
    //    mContent.assertion()
    spec
  }

  /*def onChanged(event: OnConfigChangedEvent): Unit = {
    if (event.getModID == FluidTank.modID) {
      sync()
    }
  }*/

  class Content(builder: ForgeConfigSpec.Builder) {
    builder.comment("Settings for FluidTank.").push("common")
    val removeRecipe = builder.worldRestart().comment("Remove all recipe to make tanks.")
      .define("RemoveRecipe", false)

    val enableOldRender = builder.worldRestart().comment("True to use other render system for item. It doesn't show the content of tanks.")
      .define("enableOldRender", false)

    builder.comment("Recipe settings").push(CATEGORY_RECIPE)

    private val oreNameMapValue = Tiers.list.filter(_.hasOreRecipe).map { tier =>
      (tier, builder.worldRestart().comment(s"Set OreDict name of items to craft $tier tank.")
        .define(tier.toString + "OreName", tier.oreName))
    }.toMap

    val easyRecipe = builder.comment("True to use easy recipe.").define("easyRecipe", false)

    builder.pop()

    val showInvisibleTank = builder.worldRestart().comment("True to show invisible tank in creative tabs. Recipe and block aren't removed.")
      .define("showInvisibleTankInTab", false)

    val showTOP = builder.comment("Show tank info on TOP tooltip.").define("showTOP", true)

    val enableWailaAndTOP = builder.comment("True to enable waila and top to show tank info.").define("showToolTipOnMods", true)

    builder.pop()

    def assertion(): Unit = {
      require(Tiers.list.forall(oreNameMap.contains))
    }

    def oreNameMap: Map[Tiers, String] = oreNameMapValue.view.mapValues(_.get()).toMap ++ Tiers.list.filterNot(_.hasOreRecipe).map(t => (t, t.oreName)).toMap
  }

}
