import React, { useState , useParams } from 'react';
import useCartStore from '../../../store/users/customer/cartStore';
import styles from './FoodTruckMenuDetail.module.css';

function FoodTruckMenuDetail({ menu }) {
  const menuId = useParams();
  const [quantity, setQuantity] = useState(1);
  const addItemToCart = useCartStore(state => state.addItemToCart);

  const handleIncrease = () => {
    setQuantity(quantity + 1);
  };

  const handleDecrease = () => {
    if (quantity > 1) {
      setQuantity(quantity - 1);
    }
  };

  const handleAddToCart = () => {
    const cartItem = {
      ...menu,
      quantity,
    };
    addItemToCart(cartItem);
    alert(`${menu.price * quantity}원 장바구니에 담을게요!`);
  };

  return (
    <div className={styles.menuDetail}>
      <h2 className={styles.title}>{menu?.name}</h2>
      <img src={menu?.image} alt={menu?.name} className={styles.image} />
      <div className={styles.quantityControl}>
        <button onClick={handleDecrease} className={styles.button}>-</button>
        <span className={styles.quantity}>{quantity}</span>
        <button onClick={handleIncrease} className={styles.button}>+</button>
      </div>
      <p className={styles.descriptionTitle}>상세 설명이에요!</p>
      <p className={styles.description}>{menu?.description}</p>
      <button onClick={handleAddToCart} className={styles.addToCartButton}>
        {menu?.price * quantity}원 장바구니에 담을게요!
      </button>
    </div>
  );
}

export default FoodTruckMenuDetail;
