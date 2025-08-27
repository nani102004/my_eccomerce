import { useState, useEffect } from "react";
import { RadioGroup } from "@headlessui/react";
import { useNavigate, useParams } from "react-router-dom";
import ProductReviewCard from "./ProductReviewCard";
import { Box, Button, Grid, LinearProgress, Rating } from "@mui/material";
import HomeProductCard from "../../Home/HomeProductCard";
import { useDispatch, useSelector } from "react-redux";
import { findProductById } from "../../../../Redux/Customers/Product/Action";
import { addItemToCart } from "../../../../Redux/Customers/Cart/Action";
import { getAllReviews } from "../../../../Redux/Customers/Review/Action";
import { gounsPage1 } from "../../../../Data/Gouns/gouns";

const product = {
  name: "Basic Tee 6-Pack",
  price: "₹996",
  href: "#",
  breadcrumbs: [
    { id: 1, name: "Men", href: "#" },
    { id: 2, name: "Clothing", href: "#" },
  ],
  images: [
    {
      src: "https://tailwindui.com/img/ecommerce-images/product-page-02-secondary-product-shot.jpg",
      alt: "Two each of gray, white, and black shirts laying flat.",
    },
    {
      src: "https://tailwindui.com/img/ecommerce-images/product-page-02-tertiary-product-shot-01.jpg",
      alt: "Model wearing plain black basic tee.",
    },
    {
      src: "https://tailwindui.com/img/ecommerce-images/product-page-02-tertiary-product-shot-02.jpg",
      alt: "Model wearing plain gray basic tee.",
    },
    {
      src: "https://tailwindui.com/img/ecommerce-images/product-page-02-featured-product-shot.jpg",
      alt: "Model wearing plain white basic tee.",
    },
  ],
  sizes: [
    { name: "S", inStock: true },
    { name: "M", inStock: true },
    { name: "L", inStock: true },
  ],
  description:
    'The Basic Tee 6-Pack allows you to fully express your vibrant personality with three grayscale options.',
  highlights: [
    "Hand cut and sewn locally",
    "Dyed with our proprietary colors",
    "Pre-washed & pre-shrunk",
    "Ultra-soft 100% cotton",
  ],
  details:
    'The 6-Pack includes two black, two white, and two heather gray Basic Tees.',
};

const reviews = { href: "#", average: 4, totalCount: 117 };

function classNames(...classes) {
  return classes.filter(Boolean).join(" ");
}

export default function ProductDetails() {
  const [selectedSize, setSelectedSize] = useState();
  const [activeImage, setActiveImage] = useState(null);
  const navigate = useNavigate();
  const dispatch = useDispatch();

  // ✅ only select the slice you need
  const customersProduct = useSelector((state) => state.customersProduct);

  const { productId } = useParams();
  const jwt = localStorage.getItem("jwt");

  const handleSetActiveImage = (image) => {
    setActiveImage(image);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const data = { productId, size: selectedSize?.name };
    dispatch(addItemToCart({ data, jwt }));
    navigate("/cart");
  };

  useEffect(() => {
    if (productId) {
      const data = { productId: Number(productId), jwt };
      dispatch(findProductById(data));
      dispatch(getAllReviews(productId));
    }
  }, [productId, dispatch, jwt]);

  return (
    <div className="bg-white lg:px-20">
      <div className="pt-6">
        {/* Breadcrumb */}
        <nav aria-label="Breadcrumb">
          <ol className="mx-auto flex max-w-2xl items-center space-x-2 px-4 sm:px-6 lg:max-w-7xl lg:px-8">
            {product.breadcrumbs.map((breadcrumb) => (
              <li key={breadcrumb.id}>
                <div className="flex items-center">
                  <a
                    href="/"
                    className="mr-2 text-sm font-medium text-gray-900"
                  >
                    {breadcrumb.name}
                  </a>
                  <svg
                    width={16}
                    height={20}
                    viewBox="0 0 16 20"
                    fill="currentColor"
                    aria-hidden="true"
                    className="h-5 w-4 text-gray-300"
                  >
                    <path d="M5.697 4.34L8.98 16.532h1.327L7.025 4.341H5.697z" />
                  </svg>
                </div>
              </li>
            ))}
            <li className="text-sm">
              <a
                href={product.href}
                aria-current="page"
                className="font-medium text-gray-500 hover:text-gray-600"
              >
                {product.name}
              </a>
            </li>
          </ol>
        </nav>

        {/* Product details */}
        <section className="grid grid-cols-1 gap-x-8 gap-y-10 lg:grid-cols-2 px-4 pt-10">
          {/* Images */}
          <div className="flex flex-col items-center">
            <div className="overflow-hidden rounded-lg max-w-[30rem] max-h-[35rem]">
              <img
                src={activeImage?.src || customersProduct.product?.imageUrl}
                alt="product"
                className="h-full w-full object-cover object-center"
              />
            </div>
            <div className="flex flex-wrap space-x-5 justify-center">
              {product.images.map((image, i) => (
                <div
                  key={i}
                  onClick={() => handleSetActiveImage(image)}
                  className="overflow-hidden rounded-lg max-w-[5rem] max-h-[5rem] mt-4 cursor-pointer"
                >
                  <img
                    src={image.src}
                    alt={image.alt}
                    className="h-full w-full object-cover object-center"
                  />
                </div>
              ))}
            </div>
          </div>

          {/* Info */}
          <div className="lg:col-span-1 mx-auto max-w-2xl px-4 pb-16 sm:px-6 lg:max-w-7xl lg:px-8 lg:pb-24">
            <h1 className="text-lg lg:text-xl font-semibold text-gray-900">
              {customersProduct.product?.brand}
            </h1>
            <h2 className="text-lg lg:text-xl text-gray-900 opacity-60 pt-1">
              {customersProduct.product?.title}
            </h2>

            {/* Price */}
            <div className="flex space-x-5 items-center text-lg mt-6">
              <p className="font-semibold">
                ₹{customersProduct.product?.discountedPrice}
              </p>
              <p className="opacity-50 line-through">
                ₹{customersProduct.product?.price}
              </p>
              <p className="text-green-600 font-semibold">
                {customersProduct.product?.discountPersent}% Off
              </p>
            </div>

            {/* Reviews */}
            <div className="mt-6 flex items-center space-x-3">
              <Rating value={4.6} precision={0.5} readOnly />
              <p className="opacity-60 text-sm">42807 Ratings</p>
              <p className="ml-3 text-sm font-medium text-indigo-600">
                {reviews.totalCount} reviews
              </p>
            </div>

            {/* Size & Add to cart */}
            <form className="mt-10" onSubmit={handleSubmit}>
              <h3 className="text-sm font-medium text-gray-900">Size</h3>
              <RadioGroup
                value={selectedSize}
                onChange={setSelectedSize}
                className="mt-4"
              >
                <div className="grid grid-cols-4 gap-4 sm:grid-cols-8 lg:grid-cols-10">
                  {product.sizes.map((size) => (
                    <RadioGroup.Option
                      key={size.name}
                      value={size}
                      disabled={!size.inStock}
                      className={({ active }) =>
                        classNames(
                          size.inStock
                            ? "cursor-pointer bg-white text-gray-900 shadow-sm"
                            : "cursor-not-allowed bg-gray-50 text-gray-200",
                          active ? "ring-1 ring-indigo-500" : "",
                          "group relative flex items-center justify-center rounded-md border py-1 px-1 text-sm font-medium uppercase"
                        )
                      }
                    >
                      {({ active, checked }) => (
                        <>
                          <RadioGroup.Label as="span">
                            {size.name}
                          </RadioGroup.Label>
                          {checked && (
                            <span
                              className="pointer-events-none absolute -inset-px rounded-md border-2 border-indigo-500"
                              aria-hidden="true"
                            />
                          )}
                        </>
                      )}
                    </RadioGroup.Option>
                  ))}
                </div>
              </RadioGroup>

              <Button
                variant="contained"
                type="submit"
                sx={{ padding: ".8rem 2rem", marginTop: "2rem" }}
              >
                Add To Cart
              </Button>
            </form>

            {/* Description */}
            <div className="py-10">
              <p className="text-base text-gray-900">
                {customersProduct.product?.description}
              </p>
            </div>
          </div>
        </section>

        {/* Reviews Section */}
        <section className="mt-10">
          <h1 className="font-semibold text-lg pb-4">
            Recent Review & Ratings
          </h1>
          <div className="border p-5">
            <Grid container spacing={7}>
              <Grid size={{ xs: 7 }}>
                <div className="space-y-5">
                  {customersProduct.product?.reviews?.map((item, i) => (
                    <ProductReviewCard key={i} item={item} />
                  ))}
                </div>
              </Grid>
              <Grid size={{ xs: 5 }}>
                <h1 className="text-xl font-semibold pb-1">Product Ratings</h1>
                <div className="flex items-center space-x-3 pb-10">
                  <Rating value={4.6} precision={0.5} readOnly />
                  <p className="opacity-60">42807 Ratings</p>
                </div>
              </Grid>
            </Grid>
          </div>
        </section>

        {/* Similar Products */}
        <section className="pt-10">
          <h1 className="py-5 text-xl font-bold">Similar Products</h1>
          <div className="flex flex-wrap space-y-5">
            {gounsPage1.map((item) => (
              <HomeProductCard key={item.id} product={item} />
            ))}
          </div>
        </section>
      </div>
    </div>
  );
}
