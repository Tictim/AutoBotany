package tictim.autobotany.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IRegistryDelegate;
import net.minecraftforge.registries.tags.ITag;
import net.minecraftforge.registries.tags.ITagManager;

public abstract sealed class FluidMatch{
	public static FluidMatch read(CompoundTag tag){
		String type = tag.getString("Type");
		switch(type){
			case ExactMatch.TYPE -> {
				Fluid fluid = ABNBT.readFluid(tag.getString("Fluid"));
				return fluid!=null ? new ExactMatch(fluid) : NoMatch.INSTANCE;
			}
			case TagMatch.TYPE -> {
				ResourceLocation res = ABNBT.readNamespace(tag.getString("Tag"));
				return res!=null ? new TagMatch(res) : NoMatch.INSTANCE;
			}
			case NoMatch.TYPE -> {
				return NoMatch.INSTANCE;
			}
			default -> {
				if(ABNBT.REPORT_NBT_ERROR)
					ABNBT.logNbtReadError("Invalid fluid match type '"+type+'\'');
				return NoMatch.INSTANCE;
			}
		}
	}

	public abstract String type();
	public abstract boolean matches(Fluid fluid);

	public CompoundTag write(){
		CompoundTag tag = new CompoundTag();
		writeInternal(tag);
		tag.putString("Type", type());
		return tag;
	}
	protected abstract void writeInternal(CompoundTag tag);

	public static final class ExactMatch extends FluidMatch{
		public static final String TYPE = "exact";

		private final IRegistryDelegate<Fluid> fluid;

		public ExactMatch(Fluid fluid){
			this(fluid.delegate);
		}
		public ExactMatch(IRegistryDelegate<Fluid> fluid){
			this.fluid = fluid;
		}

		@Override public String type(){
			return TYPE;
		}
		@Override public boolean matches(Fluid fluid){
			return this.fluid.get().isSame(fluid);
		}
		@Override protected void writeInternal(CompoundTag tag){
			tag.putString("Fluid", fluid.name().toString());
		}
	}

	public static final class TagMatch extends FluidMatch{
		public static final String TYPE = "tag";

		private final TagKey<Fluid> tag;

		public TagMatch(ResourceLocation tag){
			this(TagKey.create(ForgeRegistries.Keys.FLUIDS, tag));
		}
		public TagMatch(TagKey<Fluid> tag){
			this.tag = tag;
		}

		@Override public String type(){
			return TYPE;
		}
		@Override public boolean matches(Fluid fluid){
			ITagManager<Fluid> tags = ForgeRegistries.FLUIDS.tags();
			if(tags==null||!tags.isKnownTagName(this.tag)) return false;
			ITag<Fluid> tag = tags.getTag(this.tag);
			return tag.contains(fluid);
		}
		@Override protected void writeInternal(CompoundTag tag){
			tag.putString("Tag", this.tag.location().toString());
		}
	}

	public static final class NoMatch extends FluidMatch{
		public static final String TYPE = "";
		public static final NoMatch INSTANCE = new NoMatch();

		private NoMatch(){}

		@Override public String type(){
			return TYPE;
		}
		@Override public boolean matches(Fluid fluid){
			return false;
		}
		@Override protected void writeInternal(CompoundTag tag){}
	}
}
