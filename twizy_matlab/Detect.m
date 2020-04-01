close all
clear all
clc

% detection panneaux

pan70 = imread('p2.jpg');
ref70 = imread('ref30.jpg');

figure, imshow(pan70);

pan70gray = rgb2gray(pan70);

%% reconnaissance contour
pan70 = im2double(pan70);
pan70HSV = rgb2hsv(pan70);
figure; imhist(pan70HSV(:,:,1));

mpan70Rouge = (pan70HSV(:,:,1)>0.97)&(pan70HSV(:,:,1)<0.99);
figure, imshow(mpan70Rouge);


%% extraction contour

SE2= strel('disk',6);
dilatedpan70 = imdilate(mpan70Rouge,SE2);
figure, imshow(dilatedpan70);

BWdilatedIpions = edge(dilatedpan70, 'Canny');
figure, imshow(BWdilatedIpions);
hold on;

[centers, radii, metric] = imfindcircles(BWdilatedIpions,[30 150],'Sensitivity',0.90);
viscircles(centers, radii, 'LineStyle', '-');


%% extraction image

px = centers(1);  
py = centers(2);
r = radii;

pan70gray2 = imcrop(pan70gray,[px-r py-r 2*r 2*r]);
figure, imshow(pan70gray2);

% template matching 

corners = detectSURFFeatures(pan70gray2);
[features, valid_corners] = extractFeatures(pan70gray2, corners);
figure, imshow(pan70gray2);
hold on,plot(valid_corners);

ref70gray = rgb2gray(ref70);
corners2 = detectSURFFeatures(ref70gray);
[features2, valid_corners2] = extractFeatures(ref70gray, corners2);
figure, imshow(ref70gray);
hold on,plot(valid_corners2);

indexPairs = matchFeatures(features,features2);
matchedPoints1 = valid_corners(indexPairs(:,1),:);
matchedPoints2 = valid_corners2(indexPairs(:,2),:);

figure;ax = axes; showMatchedFeatures(pan70gray2,ref70gray,matchedPoints1,matchedPoints2,'montage','Parent',ax);

