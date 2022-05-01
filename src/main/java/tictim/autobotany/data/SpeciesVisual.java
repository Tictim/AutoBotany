package tictim.autobotany.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import tictim.autobotany.crop.Crop;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public final class SpeciesVisual{
	public static Builder visual(){
		return new Builder();
	}
	public static SpeciesVisual defaultVisual(ResourceLocation id, int stages){
		return defaultVisual(id.getNamespace(), id.getPath(), stages);
	}
	public static SpeciesVisual defaultVisual(String namespace, String path, int stages){
		return visual()
				.addDefaultStages(namespace, path, stages)
				.defaultMature(namespace, path)
				.build();
	}

	public static CropModel.Builder model(String path){
		return model("minecraft", path);
	}
	public static CropModel.Builder model(String namespace, String path){
		return model(new ResourceLocation(namespace, path));
	}
	public static CropModel.Builder model(ResourceLocation model){
		return new CropModel.Builder(model);
	}

	private final List<CropModel> growing;
	private final CropModel mature;

	public SpeciesVisual(List<CropModel> growing, CropModel mature){
		for(CropModel s : growing) Objects.requireNonNull(s);
		this.growing = List.copyOf(growing);
		this.mature = Objects.requireNonNull(mature);
	}

	public List<CropModel> growingStages(){
		return growing;
	}

	public CropModel growing(int stage){
		return growing.get(stage);
	}
	public CropModel mature(){
		return mature;
	}

	public CropModel model(Crop crop){
		if(crop.isFullyGrown()||growing.isEmpty()) return mature();
		if(growing.size()==1) return growing.get(0);
		int i = Mth.clamp((int)(growing.size()*crop.growthPercent()), 0, growing.size()-1);
		return growing.get(i);
	}

	public void forEachModels(Consumer<ResourceLocation> consumer){
		for(CropModel cropModel : growing){
			cropModel.forEachModel(consumer);
		}
		mature.forEachModel(consumer);
	}

	public record CropModel(ResourceLocation model, int color, @Nullable ResourceLocation wiltModel, boolean wiltColorOverride, int wiltColor){
		public CropModel(ResourceLocation model, int color, @Nullable ResourceLocation wiltModel, boolean wiltColorOverride, int wiltColor){
			this.model = Objects.requireNonNull(model);
			this.color = color;
			this.wiltModel = wiltModel;
			this.wiltColorOverride = wiltColorOverride;
			this.wiltColor = wiltColor;
		}

		public ResourceLocation model(boolean wilt){
			return wilt&&wiltModel!=null ? wiltModel : model;
		}
		/**
		 * @return RGB
		 */
		public int color(boolean wilt){
			return wilt&&wiltColorOverride ? wiltColor : color;
		}

		public void forEachModel(Consumer<ResourceLocation> res){
			res.accept(model);
			if(wiltModel!=null) res.accept(wiltModel);
		}

		@Override public String toString(){
			String s = model.toString();
			if((color|0xFF000000)!=0xFFFFFFFF){
				s += "#c:"+Integer.toHexString(color&0xFFFFFF);
			}
			if(wiltModel!=null||wiltColorOverride){
				s += ",wilt:";
				if(wiltModel!=null) s += wiltModel;
				if(wiltColorOverride) s += "#c:"+Integer.toHexString(wiltColor&0xFFFFFF);
			}
			return s;
		}

		public static final class Builder{
			private final ResourceLocation model;
			private int color = 0xFFFFFFFF;

			@Nullable private ResourceLocation wiltModel;
			private boolean wiltColorOverride;
			private int wiltColor;

			public Builder(ResourceLocation model){
				this.model = model;
			}

			public Builder color(int color){
				this.color = color;
				return this;
			}
			public Builder wilt(String path){
				return wilt(new ResourceLocation(path));
			}
			public Builder wilt(String namespace, String path){
				return wilt(new ResourceLocation(namespace, path));
			}
			public Builder wilt(ResourceLocation model){
				this.wiltModel = model;
				this.wiltColorOverride = false;
				return this;
			}
			public Builder wilt(int color){
				this.wiltModel = null;
				this.wiltColorOverride = true;
				this.wiltColor = color;
				return this;
			}

			public Builder wilt(String path, int color){
				return wilt(new ResourceLocation(path), color);
			}
			public Builder wilt(String namespace, String path, int color){
				return wilt(new ResourceLocation(namespace, path), color);
			}
			public Builder wilt(ResourceLocation model, int color){
				this.wiltModel = model;
				this.wiltColorOverride = true;
				this.color = color;
				return this;
			}
			public CropModel build(){
				return new CropModel(model, color, wiltModel, wiltColorOverride, wiltColor);
			}
		}
	}

	public static final class Builder{
		private final List<CropModel> growing = new ArrayList<>();
		@Nullable private CropModel mature;

		public Builder(){}

		public Builder addDefaultStages(String namespace, String path, int stages){
			for(int i = 0; i<stages; i++)
				addDefaultStage(namespace, path);
			return this;
		}
		public Builder addDefaultStage(String namespace, String path){
			return addStage(model(namespace, "autobotany_crop/"+path+"_"+growing.size())
					.wilt(namespace, "autobotany_crop/"+path+"_"+growing.size()+"_wilt"));
		}
		public Builder addStage(CropModel.Builder model){
			return addStage(model.build());
		}
		public Builder addStage(CropModel model){
			this.growing.add(model);
			return this;
		}

		public Builder defaultMature(String namespace, String path){
			return mature(model(namespace, "autobotany_crop/"+path+"_mature")
					.wilt(namespace, "autobotany_crop/"+path+"_mature_wilt"));
		}
		public Builder mature(CropModel.Builder model){
			return mature(model.build());
		}
		public Builder mature(CropModel model){
			this.mature = model;
			return this;
		}

		public SpeciesVisual build(){
			if(mature==null) throw new IllegalStateException("Mature visual not provided");
			return new SpeciesVisual(growing, mature);
		}
	}
}
