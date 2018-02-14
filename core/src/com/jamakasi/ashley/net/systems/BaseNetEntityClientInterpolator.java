package com.jamakasi.ashley.net.systems;

import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.IntMap.Entry;
import com.badlogic.gdx.utils.TimeUtils;
import com.jamakasi.ashley.net.entity.BaseNetEntity;

/**
 *
 * @author jamakasi
 */
public class BaseNetEntityClientInterpolator extends IntervalSystem{
    float tickIntervalMS;
    int tickIntervalSEC;
    BaseNetClientSystem clientSystem;
    /**
     * 
     * @param interval in secs. Example 60 is 60per second
     */
    public BaseNetEntityClientInterpolator(int interval,BaseNetClientSystem clientSystem) {
        super((float)1/interval);
        tickIntervalMS = (float)1/interval;
        tickIntervalSEC = interval;
        this.clientSystem = clientSystem;
    }

    @Override
    protected void updateInterval() {
        IntMap<BaseNetEntity> netEntitys = clientSystem.netEntityManager.getNetEntitys();
        //int clientTickIntervalSec = clientSystem.tickIntervalSEC;
        for(Entry<BaseNetEntity> entry:netEntitys.entries()){
            BaseNetEntity netEnt = entry.value;
            if(netEnt == clientSystem.clientHelper.playerEntity){
                
            }else{
                netEnt.getPosition().set(netEnt.oldNetTransform.position.
                        lerp(netEnt.nextNetTransform.position,
                                getInterpolationAlpha(netEnt.lastUpdateTime, tickIntervalSEC )) );//clientTickIntervalSec
                netEnt.getScale().set(netEnt.oldNetTransform.size.
                        lerp(netEnt.nextNetTransform.size,
                                getInterpolationAlpha(netEnt.lastUpdateTime, tickIntervalSEC )) );
                netEnt.getPitchYawRoll().set(netEnt.oldNetTransform.pitchYawRoll.
                        slerp(netEnt.nextNetTransform.pitchYawRoll,
                                getInterpolationAlpha(netEnt.lastUpdateTime, tickIntervalSEC )) );
                netEnt.lastUpdateTime = TimeUtils.millis();
                //netEnt.getTransform().set(netEnt.nextNetTransform);
            }
            
        }
    }
    /**
    * 
    * @param lastUpdateTime 
    * @param clientTickInteravl
    * @return 
    */
    public float getInterpolationAlpha(long lastUpdateTime, float clientTickInteravl) {
        float alpha = (TimeUtils.millis() - lastUpdateTime) / clientTickInteravl;
        return MathUtils.clamp(alpha, 0f, 1.0f);
    }
}
