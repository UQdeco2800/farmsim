import farmsim.particle.*;
import farmsim.tiles.TileRegister;

        try {
        } catch (Exception e) { }

        return false;
        double x = (animal.getLocation().getX() + animal.targetMate
                .getLocation().getX()) /2;
        double y = (animal.getLocation().getY() + animal.targetMate
                .getLocation().getY()) /2;
        displayHeart(x, y);
    protected void displayHeart(double x, double y) {
        Emitter emitter = new Emitter(ParticleController.getInstance().getParent());
        TileRegister tileRegister = TileRegister.getInstance();
        ParticleController.getInstance().addParticleSet(emitter);
        emitter.setBounding(BoundType.WORLD);
        emitter.setDeathCondition(DeathType.TIME, 20);
        emitter.setCords(new ParticleVector(x * tileRegister.TILE_SIZE + 8, y *
                tileRegister.TILE_SIZE +8));
        emitter.setSpawnRate(new ParticleVector(1, 1));
        emitter.setVelocity(new ParticleVector(0, -2));
        emitter.setTexture(tileRegister.getTileImage("heart16"));
        emitter.spawn();
        emitter.kill();
    }
