package mekanism.common.tile;

import javax.annotation.Nonnull;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.Mekanism;
import mekanism.common.content.matrix.MatrixCache;
import mekanism.common.content.matrix.MatrixUpdateProtocol;
import mekanism.common.content.matrix.SynchronizedMatrixData;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableDouble;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.multiblock.MultiblockManager;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.registries.MekanismTileEntityTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntityInductionCasing extends TileEntityMultiblock<SynchronizedMatrixData> {

    public TileEntityInductionCasing() {
        this(MekanismBlocks.INDUCTION_CASING);
    }

    public TileEntityInductionCasing(IBlockProvider blockProvider) {
        super(blockProvider);
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        if (structure != null && isRendering) {
            //We tick the structure before adding/draining from the slots, so that we make sure they get
            // first "pickings" at attempting to get or give power, without having to worry about the
            // rate limit of the structure being used up by the ports
            structure.tick();
            structure.energyInputSlot.drainContainer();
            structure.energyOutputSlot.fillContainerOrConvert();
        }
    }

    @Override
    public boolean isCapabilityDisabled(@Nonnull Capability<?> capability, Direction side) {
        //Disable item handler caps if we are the induction casing, don't disable it for the subclassed port though
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && getType() == MekanismTileEntityTypes.INDUCTION_CASING.getTileEntityType()) {
            return true;
        }
        return super.isCapabilityDisabled(capability, side);
    }

    @Override
    public ActionResultType onActivate(PlayerEntity player, Hand hand, ItemStack stack) {
        return structure == null ? ActionResultType.PASS : openGui(player);
    }

    @Nonnull
    @Override
    protected SynchronizedMatrixData getNewStructure() {
        return new SynchronizedMatrixData(this);
    }

    @Override
    public MatrixCache getNewCache() {
        return new MatrixCache();
    }

    @Override
    protected MatrixUpdateProtocol getProtocol() {
        return new MatrixUpdateProtocol(this);
    }

    @Override
    public MultiblockManager<SynchronizedMatrixData> getManager() {
        return Mekanism.matrixManager;
    }

    //TODO: Stash the cached client values here rather than in the structure, that way we can easier handle zero
    public double getEnergy() {
        //Uses post queue as that is the actual total we just haven't saved it yet
        return structure == null ? 0 : structure.getEnergy();
    }

    public double getMaxEnergy() {
        return structure == null ? 0 : structure.getStorageCap();
    }

    public double getLastInput() {
        return structure == null ? 0 : structure.getLastInput();
    }

    public double getLastOutput() {
        return structure == null ? 0 : structure.getLastOutput();
    }

    public double getTransferCap() {
        return structure == null ? 0 : structure.getTransferCap();
    }

    public int getCellCount() {
        return structure == null ? 0 : structure.getCellCount();
    }

    public int getProviderCount() {
        return structure == null ? 0 : structure.getProviderCount();
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableDouble.create(this::getEnergy, value -> {
            if (structure != null) {
                structure.setClientEnergy(value);
            }
        }));
        container.track(SyncableDouble.create(this::getMaxEnergy, value -> {
            if (structure != null) {
                structure.setClientMaxEnergy(value);
            }
        }));
        container.track(SyncableDouble.create(this::getLastInput, value -> {
            if (structure != null) {
                structure.setClientLastInput(value);
            }
        }));
        container.track(SyncableDouble.create(this::getLastOutput, value -> {
            if (structure != null) {
                structure.setClientLastOutput(value);
            }
        }));
    }

    public void addStatsTabContainerTrackers(MekanismContainer container) {
        container.track(SyncableDouble.create(() -> structure == null ? 0 : structure.getTransferCap(), value -> {
            if (structure != null) {
                structure.setClientMaxTransfer(value);
            }
        }));
        container.track(SyncableInt.create(() -> structure == null ? 0 : structure.volHeight, value -> {
            if (structure != null) {
                structure.volHeight = value;
            }
        }));
        container.track(SyncableInt.create(() -> structure == null ? 0 : structure.volWidth, value -> {
            if (structure != null) {
                structure.volWidth = value;
            }
        }));
        container.track(SyncableInt.create(() -> structure == null ? 0 : structure.volLength, value -> {
            if (structure != null) {
                structure.volLength = value;
            }
        }));
        container.track(SyncableInt.create(() -> structure == null ? 0 : structure.getCellCount(), value -> {
            if (structure != null) {
                structure.setClientCells(value);
            }
        }));
        container.track(SyncableInt.create(() -> structure == null ? 0 : structure.getProviderCount(), value -> {
            if (structure != null) {
                structure.setClientProviders(value);
            }
        }));
    }
}