import { motion } from "framer-motion";
import { FaGlobe } from "react-icons/fa";
const VolunteerBadge = () => {
    return (
        <div className="flex items-center justify-center min-h-screen bg-white perspective-[1000px]">
          <motion.div
            animate={{ rotateY: 360 }}
            transition={{
              repeat: Infinity,
              duration: 4,
              ease: "linear",
            }}
            style={{
              transformStyle: "preserve-3d",
              fontSize: "120px",
              position: "relative",
            }}
          >
            {/* Front side of the globe */}
            <div
              style={{
                backfaceVisibility: "hidden",
                position: "absolute",
                inset: 0,
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
              }}
            >
              🌍
            </div>
    
            {/* Back side of the globe (mirrored) */}
            <div
              style={{
                transform: "rotateY(180deg) scaleX(-1)",
                backfaceVisibility: "hidden",
                position: "absolute",
                inset: 0,
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
              }}
            >
              🌍
            </div>
          </motion.div>
        </div>
      );
};

export default VolunteerBadge;