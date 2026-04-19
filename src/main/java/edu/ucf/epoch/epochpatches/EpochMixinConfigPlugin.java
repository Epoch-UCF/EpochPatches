package edu.ucf.epoch.epochpatches;

import com.bawnorton.mixinsquared.canceller.MixinCancellerRegistrar;
import edu.ucf.epoch.epochpatches.asm.Transformers;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;

import java.util.List;
import java.util.Set;

public class EpochMixinConfigPlugin implements IMixinConfigPlugin {
	public EpochMixinConfigPlugin() {
		MixinCancellerRegistrar.register((targetClassNames, mixinClassName) ->
				                                 mixinClassName.equals("com.simibubi.create.foundation.mixin.ItemStackMixin"));
	}
	
	@Override
	public void onLoad(String mixinPackage) {}
	
	@Override
	public String getRefMapperConfig() {
		return null;
	}
	
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return true;
	}
	
	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}
	
	@Override
	public List<String> getMixins() {
		return List.of(Transformers.makeTargetingMixin());
	}
	
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
	
	private static final String AVOID_BLOCK_GOAL = "com/jamiedev/bygone/common/entity/ai/AvoidBlockGoal";
	private static final String AVOID_BLOCK_NEW = "edu/ucf/epoch/epochpatches/impl/goals/AvoidBlockGoal_POI";
	
	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		Transformers.executeTransformer(mixinClassName, targetClass);
		if (mixinClassName.equals("edu.ucf.epoch.epochpatches.mixin.server.optimizations.bygone.MSabeastEntity")) {
			for (MethodNode method : targetClass.methods) {
				if (method.name.equals("registerGoals")) {
					for (AbstractInsnNode it : method.instructions) { // change replacement to our new type
						if (it instanceof MethodInsnNode node && node.name.endsWith("usePoiInstead")) {
							node.desc = node.desc.replace(AVOID_BLOCK_GOAL, AVOID_BLOCK_NEW);
							break;
						}
					}
				} else if (method.name.contains("usePoiInstead")) {
					method.desc = method.desc.replace(AVOID_BLOCK_GOAL, AVOID_BLOCK_NEW);
				}
			}
		}
		
	}
}
